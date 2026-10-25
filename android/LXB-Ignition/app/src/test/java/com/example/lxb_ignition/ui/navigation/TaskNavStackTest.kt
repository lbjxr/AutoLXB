package com.example.lxb_ignition.ui.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TaskNavStackTest {

    // Page ids mirror the TASKS_PAGE_* constants in MainActivity.kt.
    private val home = 0
    private val list = 3
    private val form = 4
    private val editor = 2

    @Test
    fun root_containsOnlyHomeAndCannotGoBack() {
        val stack = TaskNavStack.root(home)
        assertEquals(home, stack.current)
        assertEquals(1, stack.size)
        assertFalse(stack.canGoBack(home))
        assertEquals(stack, stack.pop(home))
    }

    @Test
    fun push_appendsAndDuplicatePushIsNoOp() {
        val stack = TaskNavStack.root(home).push(list).push(form)
        assertEquals(listOf(home, list, form), stack.pages)
        assertEquals(form, stack.current)
        assertEquals(stack, stack.push(form))
    }

    @Test
    fun pop_returnsToPreviousPageThenStaysAtHome() {
        val stack = TaskNavStack.root(home).push(list).push(form)
        val backToList = stack.pop(home)
        assertEquals(listOf(home, list), backToList.pages)
        val backHome = backToList.pop(home)
        assertEquals(listOf(home), backHome.pages)
        assertEquals(backHome, backHome.pop(home))
    }

    @Test
    fun pop_fromSingleNonHomePageGoesHome() {
        val stack = TaskNavStack(listOf(form))
        assertTrue(stack.canGoBack(home))
        assertEquals(listOf(home), stack.pop(home).pages)
    }

    @Test
    fun popTo_truncatesToLastOccurrenceAndIgnoresAbsentPage() {
        val stack = TaskNavStack.root(home).push(list).push(form).push(editor)
        assertEquals(listOf(home, list), stack.popTo(list).pages)
        assertEquals(stack, stack.popTo(99))
    }

    @Test
    fun popToOrReset_truncatesWhenPresentAndResetsWhenAbsent() {
        val present = TaskNavStack.root(home).push(list).push(form)
        assertEquals(listOf(home, list), present.popToOrReset(list, home).pages)
        // Save from a form reached via the workflow form: the list is not
        // on the stack, so the result must be home -> list (Back from the
        // list then goes home, never back into the form just saved).
        val absent = TaskNavStack(listOf(home, 5, 6, form))
        assertEquals(listOf(home, list), absent.popToOrReset(list, home).pages)
        assertEquals(listOf(home), absent.popToOrReset(home, home).pages)
    }

    @Test
    fun resetTo_buildsHomeThenPage() {
        assertEquals(listOf(home, form), TaskNavStack.resetTo(form, home).pages)
        assertEquals(listOf(home), TaskNavStack.resetTo(home, home).pages)
    }

    @Test
    fun exampleFlow_listFormBackGoesToListNotHome() {
        // The batch-3 headline case: template list -> form -> Back must
        // return to the list, not straight to the Tasks home page.
        val stack = TaskNavStack.root(home).push(list).push(form)
        assertEquals(list, stack.pop(home).current)
        assertEquals(home, stack.pop(home).pop(home).current)
    }
}
