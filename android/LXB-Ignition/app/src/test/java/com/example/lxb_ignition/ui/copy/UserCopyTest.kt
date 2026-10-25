package com.example.lxb_ignition.ui.copy

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class UserCopyTest {

    @Test
    fun runtimeMessage_mapsPhaseCodes() {
        assertEquals("正在选择应用", UserCopy.runtimeMessage("APP_RESOLVE", "zh"))
        assertEquals("Selecting app", UserCopy.runtimeMessage("APP_RESOLVE", "en"))
        assertEquals("正在复用任务路线", UserCopy.runtimeMessage("SCRIPT_ACT", "zh"))
        assertEquals("正在用视觉方式执行", UserCopy.runtimeMessage("VISION_ACT", "zh"))
    }

    @Test
    fun runtimeMessage_dropsCodePrefixButKeepsDetail() {
        assertEquals(
            "任务路线回放完成: steps=3.",
            UserCopy.runtimeMessage("SCRIPT_ACT task-map replay completed: steps=3.", "zh")
        )
        assertEquals(
            "任务路线不可用，改用视觉方式执行：network error",
            UserCopy.runtimeMessage("SCRIPT_ACT task-map replay fell back to VISION_ACT: network error", "zh")
        )
        assertEquals(
            "Task route replay completed: steps=3.",
            UserCopy.runtimeMessage("SCRIPT_ACT task-map replay completed: steps=3.", "en")
        )
    }

    @Test
    fun runtimeMessage_replacesTokensInFreeText() {
        assertEquals(
            "正在将请求拆分为子任务…",
            UserCopy.runtimeMessage("Decomposing the request into sub-tasks...", "zh")
        )
        assertEquals(
            "启用拆解步骤后生效",
            UserCopy.runtimeMessage("启用 TASK_DECOMPOSE 后生效", "zh")
        )
    }

    @Test
    fun runtimeMessage_failureMessagesCarryActionableAdvice() {
        // Batch 4: exact strings taken from MainViewModel / TraceEventMapper.
        assertEquals(
            "服务未运行，请先在首页启动核心服务。",
            UserCopy.runtimeMessage("Server is not running, please start the service first.", "zh")
        )
        assertEquals(
            "核心服务未运行，请先在首页启动核心服务。",
            UserCopy.runtimeMessage("lxb-core is not running, please start the service on the home tab.", "zh")
        )
        assertEquals(
            "视觉动作输出无效，已停止任务。请检查模型配置后重试。",
            UserCopy.runtimeMessage("Vision action output was invalid, stopping this task.", "zh")
        )
        assertEquals(
            "检测到重复无效动作，已停止以避免循环。请调整任务描述后重试。",
            UserCopy.runtimeMessage("Repeated ineffective actions detected, stopping to avoid loop.", "zh")
        )
        assertEquals(
            "模型规划调用失败：timeout。请检查模型配置与网络后重试。",
            UserCopy.runtimeMessage("Planner call failed: timeout", "zh")
        )
        assertEquals(
            "Planner call failed: timeout. Check the model configuration and network, then retry.",
            UserCopy.runtimeMessage("Planner call failed: timeout", "en")
        )
        assertEquals(
            "无线启动失败：no device。请检查无线调试与配对后重试。",
            UserCopy.runtimeMessage("Wireless bootstrap action failed: no device", "zh")
        )
        assertEquals(
            "任务提交失败：core down。请检查核心服务是否已启动后重试。",
            UserCopy.runtimeMessage("Task submission failed: core down", "zh")
        )
        assertEquals(
            "同步配置失败：runtime config: refused。请检查网络与核心服务后重试。",
            UserCopy.runtimeMessage("Failed to sync runtime config: refused", "zh")
        )
        assertEquals(
            "视觉输出解析失败 (attempt 1/3): bad json。请检查模型配置后重试。",
            UserCopy.runtimeMessage("Vision output parse failed (attempt 1/3): bad json", "zh")
        )
    }

    @Test
    fun runtimeMessage_returnsNullForUnmappedText() {
        assertNull(UserCopy.runtimeMessage("Task finished successfully.", "zh"))
        assertNull(UserCopy.runtimeMessage("some-random-message", "en"))
    }
}
