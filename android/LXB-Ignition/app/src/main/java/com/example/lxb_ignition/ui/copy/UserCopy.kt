package com.example.lxb_ignition.ui.copy

/**
 * Batch 1 user-language mapping for internal automation terms.
 *
 * Pure function, no Android dependencies, so it can be unit-tested without a
 * device. It is deliberately NOT a second i18n system: it only rewrites the
 * internal terms that used to leak into user-visible runtime status and log
 * lines (APP_RESOLVE / SCRIPT_ACT / VISION_ACT / TASK_DECOMPOSE / task-map).
 * Everything else passes through untouched.
 *
 * Returns null when [text] contains none of the mapped terms, so callers can
 * fall back to their existing translation table (UiMessageLocalizer / ZhMap).
 * `lang` is "zh" for Chinese and anything else for English, matching the
 * existing uiLang convention in MainViewModel.
 */
object UserCopy {

    fun runtimeMessage(text: String, lang: String): String? {
        val zh = lang == "zh"

        // --- exact runtime messages (phase codes and status details) ---
        when (text) {
            "APP_RESOLVE" -> return if (zh) "正在选择应用" else "Selecting app"
            "DEVICE_PREPARE" -> return if (zh) "正在准备设备" else "Preparing device"
            "APP_ENTER" -> return if (zh) "正在进入应用" else "Entering app"
            "SCRIPT_ACT" -> return if (zh) "正在复用任务路线" else "Reusing task route"
            "VISION_ACT" -> return if (zh) "正在用视觉方式执行" else "Executing visually"
            "TASK_DECOMPOSE" -> return if (zh) "正在拆解步骤" else "Decomposing steps"
            "APP_RESOLVE: selecting the best app for this task..." ->
                return if (zh) "正在选择最合适的应用…" else "Selecting the best app for this task…"
            "SCRIPT_ACT: replaying learned task-route script when available..." ->
                return if (zh) "正在复用已保存的任务路线…" else "Reusing the saved task route…"
            "VISION_ACT: entering vision-action loop (LLM + VLM)." ->
                return if (zh) "正在用视觉方式执行任务…" else "Executing the task visually…"
            "Decomposing the request into sub-tasks..." ->
                return if (zh) "正在将请求拆分为子任务…" else "Breaking the request into steps…"
            "Selecting app..." -> return if (zh) "正在选择应用…" else "Selecting app…"
            "Preparing device..." -> return if (zh) "正在准备设备…" else "Preparing device…"
            "Entering app..." -> return if (zh) "正在进入应用…" else "Entering app…"
            "Running learned script..." -> return if (zh) "正在复用任务路线…" else "Reusing task route…"
            "Running vision-action..." -> return if (zh) "正在用视觉方式执行…" else "Executing visually…"
            "Fallback to vision-action..." ->
                return if (zh) "任务路线不可用，改用视觉方式执行…" else "Task route unavailable, switching to visual execution…"
            "Learned script replayed." -> return if (zh) "已复用任务路线。" else "Task route replayed."
            "UI stable." -> return if (zh) "界面已稳定。" else "Screen is stable."
            "Task finished." -> return if (zh) "任务已完成。" else "Task finished."
            "Task failed." -> return if (zh) "任务失败。" else "Task failed."
        }

        // --- prefixed messages: keep the trailing detail, drop the code ---
        fun prefixed(prefix: String, zhLabel: String, enLabel: String): String? {
            if (!text.startsWith(prefix)) return null
            val rest = text.removePrefix(prefix)
            return (if (zh) zhLabel else enLabel) + rest
        }
        prefixed("APP_RESOLVE failed: ", "选择应用失败：", "Failed to select app: ")?.let { return it }
        prefixed("APP_ENTER failed: ", "进入应用失败：", "Failed to enter app: ")?.let { return it }
        prefixed("SCRIPT_ACT result: ", "任务路线执行结果：", "Task route result: ")?.let { return it }
        prefixed("SCRIPT_ACT task-map replay fell back to VISION_ACT: ", "任务路线不可用，改用视觉方式执行：", "Task route unavailable, switching to visual execution: ")?.let { return it }
        // Replay started/completed carry an optional ": steps=N." suffix, keep it.
        if (text.startsWith("SCRIPT_ACT task-map replay started")) {
            val suffix = text.removePrefix("SCRIPT_ACT task-map replay started")
            return (if (zh) "任务路线回放开始" else "Task route replay started") + suffix
        }
        if (text.startsWith("SCRIPT_ACT task-map replay completed")) {
            val suffix = text.removePrefix("SCRIPT_ACT task-map replay completed")
            return (if (zh) "任务路线回放完成" else "Task route replay completed") + suffix
        }

        // --- generic fallback: replace any remaining internal tokens in place ---
        if (text.contains("TASK_DECOMPOSE") || text.contains("SCRIPT_ACT") ||
            text.contains("VISION_ACT") || text.contains("APP_RESOLVE") ||
            text.contains("task-map") || text.contains("task_map")
        ) {
            if (zh) {
                // Chinese has no word spaces: a token pasted into Chinese text
                // ("启用 TASK_DECOMPOSE 后生效") must not leave spaces behind
                // ("启用 拆解步骤 后生效"). Strip the spaces adjacent to each
                // token before falling back to the bare replacement.
                var out = text
                val tokens = listOf(
                    "TASK_DECOMPOSE" to "拆解步骤",
                    "SCRIPT_ACT" to "任务路线执行",
                    "VISION_ACT" to "视觉执行",
                    "APP_RESOLVE" to "选择应用",
                    "task-map" to "任务路线",
                    "task_map" to "任务路线"
                )
                for ((token, replacement) in tokens) {
                    out = out.replace(" $token ", replacement)
                    out = out.replace(" $token", replacement)
                    out = out.replace("$token ", replacement)
                    out = out.replace(token, replacement)
                }
                return out
            }
            var out = text
            out = out.replace("TASK_DECOMPOSE", "step decomposition")
            out = out.replace("SCRIPT_ACT", "task route execution")
            out = out.replace("VISION_ACT", "visual execution")
            out = out.replace("APP_RESOLVE", "app selection")
            out = out.replace("task-map", "task route")
            out = out.replace("task_map", "task route")
            return out
        }
        return null
    }
}
