// Batch 6: shared components moved verbatim from MainActivity.kt.
// Signatures and behaviour are unchanged. DeleteConfirmDialog and
// EmptyStateBox use the moved tr() from ui.i18n; GlyphBadge moved with
// them because EmptyStateBox depends on it (and it owns glyphToIcon).
package com.example.lxb_ignition.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lxb_ignition.ui.i18n.tr

/**
 * Batch 1: maps the legacy text glyphs passed around MainActivity to Material
 * icons. Call sites keep passing the old glyph string, so no call-site
 * signature changes are needed in this batch; a later batch can migrate call
 * sites to ImageVector directly. Two exceptions are intentional, not leftovers:
 * a numeric glyph is a step number (readable text, not an icon), and "•" is a
 * decorative bullet rendered as a small dot. Icons beside a visible title use
 * contentDescription = null because the title already carries the meaning.
 */
fun glyphToIcon(glyph: String): ImageVector = when (glyph) {
    "⌂" -> Icons.Filled.Home
    "✓" -> Icons.Filled.CheckCircle
    "!" -> Icons.Filled.Warning
    "▶" -> Icons.Filled.PlayArrow
    "○" -> Icons.Filled.Info
    "⚙" -> Icons.Filled.Settings
    "≡", "☰", "▣", "#" -> Icons.Filled.Menu
    "↳", "➜", "☞" -> Icons.Filled.KeyboardArrowRight
    "⇄", "↺", "⌁", "☁", "∞" -> Icons.Filled.Refresh
    "⌨" -> Icons.Filled.Edit
    "⏰" -> Icons.Filled.Alarm
    "✉" -> Icons.Filled.Email
    "⚡" -> Icons.Filled.PlayArrow
    "✦", "★" -> Icons.Filled.Star
    "🔐" -> Icons.Filled.Lock
    "☑" -> Icons.Filled.CheckCircle
    "☾" -> Icons.Filled.Notifications
    "🌐" -> Icons.Filled.Info
    "⌕" -> Icons.Filled.Search
    else -> Icons.Filled.Info
}

@Composable
fun GlyphBadge(
    glyph: String,
    accentColor: Color,
    size: androidx.compose.ui.unit.Dp = 40.dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(accentColor.copy(alpha = 0.12f))
            .border(BorderStroke(1.dp, accentColor.copy(alpha = 0.18f)), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        when {
            glyph.isNotEmpty() && glyph.all { it.isDigit() } -> Text(
                text = glyph,
                color = accentColor,
                fontSize = 16.sp
            )
            glyph == "•" -> Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(accentColor)
            )
            else -> Icon(
                imageVector = glyphToIcon(glyph),
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(size * 0.55f)
            )
        }
    }
}

@Composable
fun EmptyStateBox(
    title: String,
    detail: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GlyphBadge(glyph = "○", accentColor = MaterialTheme.colorScheme.primary, size = 44.dp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = detail,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
        )
        if (actionLabel != null && onAction != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onAction,
                modifier = Modifier.heightIn(min = 48.dp)
            ) {
                Text(actionLabel)
            }
        }
    }
}

// Batch 1: shared Material 3 delete-confirm dialog. The caller passes
// already-translated title/warning (via tr()); itemName is user data and is
// shown verbatim so the dialog always names exactly what will be deleted.
// Cancel does nothing; Confirm dismisses first, then runs the (unchanged)
// delete call supplied by the caller.
@Composable
fun DeleteConfirmDialog(
    itemName: String,
    title: String,
    warning: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (itemName.isNotBlank()) {
                    Text(text = itemName, style = MaterialTheme.typography.titleSmall)
                }
                Text(
                    text = warning,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onDismiss(); onConfirm() }) {
                Text(text = tr("Delete"), color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(text = tr("Cancel")) }
        }
    )
}
