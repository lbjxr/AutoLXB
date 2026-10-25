package com.example.lxb_ignition.ui.navigation

/**
 * Batch 3 (v0.9.0): back-stack for TasksTab pages.
 *
 * Pure logic, no Android or Compose dependencies, so it can be unit-tested
 * with plain kotlinc + JUnit. Pages are the TASKS_PAGE_* Int constants
 * from MainActivity.kt; this class deliberately knows nothing about them
 * (any Int works) so it stays decoupled from the UI.
 *
 * The stack is immutable: every operation returns a new TaskNavStack (or
 * the same instance when the operation is a no-op), so assigning the
 * result to a Compose state variable always recomposes.
 *
 * Semantics, chosen to match the old direct-assignment behaviour except
 * for Back itself (which used to jump straight home):
 * - [push]: append, unless the page is already current (duplicate pushes
 *   would otherwise need two Back presses for one visible change).
 * - [pop]: drop the top page; at a single-page stack whose page is not
 *   [home], go home; at home itself, stay (the caller must then let the
 *   system handle Back, i.e. not intercept app exit).
 * - [canGoBack]: true when there is anything to pop to, including a
 *   single non-home page (e.g. a restored stack).
 * - [popTo]: truncate to the given page when it is on the stack, else
 *   unchanged; [popToOrReset] falls back to [resetTo] when the page is
 *   absent, which reproduces the old "Save goes to the list" behaviour
 *   (list back then goes home, never back into the form just saved).
 */
class TaskNavStack(val pages: List<Int>) {

    init {
        require(pages.isNotEmpty()) { "TaskNavStack must contain at least one page" }
    }

    val current: Int get() = pages.last()

    val size: Int get() = pages.size

    fun canGoBack(home: Int): Boolean = pages.size > 1 || current != home

    fun push(page: Int): TaskNavStack =
        if (page == current) this else TaskNavStack(pages + page)

    fun pop(home: Int): TaskNavStack = when {
        pages.size > 1 -> TaskNavStack(pages.dropLast(1))
        current != home -> TaskNavStack(listOf(home))
        else -> this
    }

    fun popTo(page: Int): TaskNavStack {
        val index = pages.lastIndexOf(page)
        return if (index < 0) this else TaskNavStack(pages.subList(0, index + 1))
    }

    fun popToOrReset(page: Int, home: Int): TaskNavStack =
        if (pages.contains(page)) popTo(page) else resetTo(page, home)

    override fun equals(other: Any?): Boolean =
        other is TaskNavStack && other.pages == pages

    override fun hashCode(): Int = pages.hashCode()

    override fun toString(): String = "TaskNavStack($pages)"

    companion object {
        fun root(home: Int): TaskNavStack = TaskNavStack(listOf(home))

        fun resetTo(page: Int, home: Int): TaskNavStack =
            if (page == home) root(home) else TaskNavStack(listOf(home, page))
    }
}
