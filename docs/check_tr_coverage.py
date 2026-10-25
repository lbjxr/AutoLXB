#!/usr/bin/env python3
"""Check tr("...") coverage against ZhMap in ui/i18n/UiI18n.kt.

Scope (deliberate, see docs/PRODUCT_SPEC.md):
- Batch 6 moved ZhMap from MainActivity.kt to ui/i18n/UiI18n.kt and
  MainActivity.kt's tr() is now a thin wrapper delegating there, so the
  table is read from the new file; tr() literals are scanned in both
  MainActivity.kt and ui/components/UiComponents.kt (whose dialog calls
  the same tr()). tr() is the app's user-language (uiLang) system.
  It is NOT Android stringResource: uiLang is user-settable in Config and
  differs from the system locale, so a stringResource migration would break
  the language choice. This script only finds missing Chinese entries.
- FormValidation.kt message literals are also checked, because the UI
  passes them through tr() at display time.
- UiMessageLocalizer / UserCopy are out of scope: they translate at a
  different layer and return Chinese directly for zh.

Usage:
    python3 docs/check_tr_coverage.py
Exit code 0 always; the report lists every missing key so a human can
decide translation vs. intentional pass-through (proper nouns, symbols).
"""

import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
MAIN = ROOT / "android/LXB-Ignition/app/src/main/java/com/example/lxb_ignition/MainActivity.kt"
I18N = ROOT / "android/LXB-Ignition/app/src/main/java/com/example/lxb_ignition/ui/i18n/UiI18n.kt"
COMPONENTS = (
    ROOT
    / "android/LXB-Ignition/app/src/main/java/com/example/lxb_ignition/ui/components/UiComponents.kt"
)
VALIDATION = (
    ROOT
    / "android/LXB-Ignition/app/src/main/java/com/example/lxb_ignition/ui/validation/FormValidation.kt"
)


def read_kotlin_string(text: str, start: int) -> tuple[str, int]:
    """Parse a Kotlin double-quoted string starting at text[start] == '"'.
    Returns (decoded_value, index_after_closing_quote)."""
    assert text[start] == '"'
    out = []
    i = start + 1
    while i < len(text):
        c = text[i]
        if c == "\\":
            nxt = text[i + 1] if i + 1 < len(text) else ""
            mapping = {"n": "\n", "t": "\t", "r": "\r", '"': '"', "\\": "\\", "$": "$"}
            out.append(mapping.get(nxt, nxt))
            i += 2
        elif c == '"':
            return "".join(out), i + 1
        else:
            out.append(c)
            i += 1
    raise ValueError("unterminated string literal")


def find_tr_literals(text: str) -> set[str]:
    keys = set()
    for m in re.finditer(r"\btr\(\s*", text):
        i = m.end()
        if i < len(text) and text[i] == '"':
            value, _ = read_kotlin_string(text, i)
            keys.add(value)
    return keys


def find_form_error_messages(text: str) -> set[str]:
    keys = set()
    for m in re.finditer(r"FormError\([^,]+,\s*", text):
        i = m.end()
        if i < len(text) and text[i] == '"':
            value, _ = read_kotlin_string(text, i)
            keys.add(value)
    return keys


def find_zhmap_keys(text: str) -> set[str]:
    start = text.index("val ZhMap = mapOf(")
    # Paren-depth scan from the '(' of mapOf(.
    depth = 0
    i = text.index("(", start)
    end = None
    while i < len(text):
        c = text[i]
        if c == '"':
            _, i = read_kotlin_string(text, i)
            continue
        if c == "(":
            depth += 1
        elif c == ")":
            depth -= 1
            if depth == 0:
                end = i
                break
        i += 1
    assert end is not None
    block = text[start:end]
    keys = set()
    pos = 0
    while True:
        q = block.find('"', pos)
        if q < 0:
            break
        key, after = read_kotlin_string(block, q)
        # A ZhMap entry is "key" to "value"; only accept a key followed by ' to '.
        rest = block[after : after + 10]
        if rest.strip().startswith("to"):
            keys.add(key)
        pos = after
    return keys


# Keys that reach tr() without appearing as a tr("...") literal in
# MainActivity.kt. They are listed explicitly (with their source) so the
# coverage check does not silently miss them:
# - Trigger-type chips: tr(type.replaceFirstChar(...)) for type in
#   {"none", "schedule", "notification"}.
# - Task states: tr(task.state) where state is a TaskState enum value in
#   lxb-core's CortexTaskManager (PENDING/RUNNING/COMPLETED/CANCELLED/
#   FAILED); no other state value is produced by the core.
# - Bottom-tab labels: tr(rawLabel) for the tab list in LXBIgnitionApp.
# - Wireless-guide headlines: tr(ui.headline) for the states produced by
#   resolveWirelessGuideUiState().
# - ActivitySnackbarEvent keys (batch 5): the Activity carries English
#   canonical keys and LXBIgnitionApp translates them via i18n.tr() at
#   Snackbar display time, so they never appear as tr("...") literals.
#   The action label "Retry" is translated the same way.
DYNAMIC_KEYS = {
    "None",
    "Schedule",
    "Notification",
    "PENDING",
    "RUNNING",
    "COMPLETED",
    "CANCELLED",
    "FAILED",
    "Control",
    "Tasks",
    "Config",
    "Logs",
    "Open Developer Options",
    "Waiting for pairing code",
    "Pairing phone",
    "Phone paired successfully",
    "Connecting phone",
    "Starting core service",
    "Recovering core connection",
    "Ready to use",
    "Startup failed",
    "Stopping core service",
    "Pair and connect first",
    "Not started yet",
    "Notification permission was denied. Task and runtime status may not be shown; enable notifications in system settings.",
    "Failed to open Developer Options automatically. Please open Developer Options in system settings and try again.",
    "Failed to open Wireless debugging settings automatically. Please open Wireless debugging in system settings and try again.",
    "Retry",
}


def main() -> int:
    main_text = MAIN.read_text(encoding="utf-8")
    comp_text = COMPONENTS.read_text(encoding="utf-8") if COMPONENTS.exists() else ""
    tr_keys = find_tr_literals(main_text) | find_tr_literals(comp_text) | DYNAMIC_KEYS
    zh_keys = find_zhmap_keys(I18N.read_text(encoding="utf-8"))
    extra_keys = set()
    if VALIDATION.exists():
        extra_keys = find_form_error_messages(VALIDATION.read_text(encoding="utf-8"))
    needed = tr_keys | extra_keys
    missing = sorted(needed - zh_keys)
    print(f"tr() keys (MainActivity.kt + UiComponents.kt literals + documented dynamic keys, unique): {len(tr_keys)}")
    print(f"FormValidation message literals (unique): {len(extra_keys)}")
    print(f"ZhMap keys: {len(zh_keys)}")
    print(f"needed (union): {len(needed)}")
    print(f"missing Chinese entries: {len(missing)}")
    for key in missing:
        print(f"MISSING: {key!r}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
