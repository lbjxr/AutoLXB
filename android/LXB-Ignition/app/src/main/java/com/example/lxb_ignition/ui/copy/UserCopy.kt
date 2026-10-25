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

        // --- Batch 4: common failures, "what happened + what to do" ---
        // Only strings that really occur in MainViewModel / TraceEventMapper
        // are mapped here; no error codes are invented. These run before
        // the prefixed handlers below (and before UiMessageLocalizer's own
        // table, which UserCopy feeds first), so the actionable wording
        // replaces the bare "failed: detail" translations for these cases.
        when (text) {
            "Server is not running, please start the service first." ->
                return if (zh) "服务未运行，请先在首页启动核心服务。" else "Service is not running. Start the core service on the home page first."
            "lxb-core is not running, please start the service on the home tab." ->
                return if (zh) "核心服务未运行，请先在首页启动核心服务。" else "Core service is not running. Start it on the home page first."
            "Vision action output was invalid, stopping this task." ->
                return if (zh) "视觉动作输出无效，已停止任务。请检查模型配置后重试。" else "Vision action output was invalid, so the task was stopped. Check the model configuration and retry."
            "Repeated ineffective actions detected, stopping to avoid loop." ->
                return if (zh) "检测到重复无效动作，已停止以避免循环。请调整任务描述后重试。" else "Repeated ineffective actions detected, so the task was stopped to avoid a loop. Adjust the task description and retry."
        }
        if (text.startsWith("Vision output parse failed")) {
            val suffix = text.removePrefix("Vision output parse failed")
            return (if (zh) "视觉输出解析失败" else "Vision output parse failed") + suffix +
                (if (zh) "。请检查模型配置后重试。" else ". Check the model configuration and retry.")
        }
        fun failedWithAdvice(prefix: String, zhLabel: String, enLabel: String, zhAdvice: String, enAdvice: String): String? {
            if (!text.startsWith(prefix)) return null
            val rest = text.removePrefix(prefix)
            return (if (zh) zhLabel else enLabel) + rest + (if (zh) zhAdvice else enAdvice)
        }
        failedWithAdvice("Planner call failed: ", "模型规划调用失败：", "Planner call failed: ",
            "。请检查模型配置与网络后重试。", ". Check the model configuration and network, then retry.")?.let { return it }
        failedWithAdvice("Wireless bootstrap action failed: ", "无线启动失败：", "Wireless bootstrap action failed: ",
            "。请检查无线调试与配对后重试。", ". Check wireless debugging and pairing, then retry.")?.let { return it }
        failedWithAdvice("Task submission failed: ", "任务提交失败：", "Task submission failed: ",
            "。请检查核心服务是否已启动后重试。", ". Check that the core service is running, then retry.")?.let { return it }
        failedWithAdvice("Failed to sync ", "同步配置失败：", "Failed to sync ",
            "。请检查网络与核心服务后重试。", ". Check the network and the core service, then retry.")?.let { return it }

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
