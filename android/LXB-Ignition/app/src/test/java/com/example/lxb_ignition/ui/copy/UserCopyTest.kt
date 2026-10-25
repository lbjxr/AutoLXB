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
    fun runtimeMessage_returnsNullForUnmappedText() {
        assertNull(UserCopy.runtimeMessage("Task finished successfully.", "zh"))
        assertNull(UserCopy.runtimeMessage("some-random-message", "en"))
    }
}
