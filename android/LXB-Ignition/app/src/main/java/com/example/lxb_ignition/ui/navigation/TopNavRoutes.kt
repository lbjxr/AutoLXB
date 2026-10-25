package com.example.lxb_ignition.ui.navigation

/**
 * Batch 7 (v0.9.0): top-level bottom-tab routes for the NavHost in
 * LXBIgnitionApp (MainActivity.kt).
 *
 * Pure logic, no Android or Compose dependencies, so it can be unit-tested
 * with plain kotlinc + JUnit. The order of [all] is the bottom-bar order
 * and must match the tabs list in LXBIgnitionApp (Control, Tasks, Config,
 * Logs); [tabIndexForRoute] / [routeForTabIndex] are the only mapping
 * between a route string and a bottom-bar index, so the two can never
 * drift apart silently.
 *
 * In-tab page stacks (Tasks/Control/Config, batches 3-4) are a separate
 * mechanism and are untouched by this file: this object only names the
 * four top-level destinations.
 */
object TopNavRoutes {

    const val CONTROL = "control"
    const val TASKS = "tasks"
    const val CONFIG = "config"
    const val LOGS = "logs"

    /** Bottom-bar order. [CONTROL] is the NavHost start destination. */
    val all: List<String> = listOf(CONTROL, TASKS, CONFIG, LOGS)

    /** Bottom-bar index for a route, or null when the route is unknown. */
    fun tabIndexForRoute(route: String?): Int? = when (route) {
        CONTROL -> 0
        TASKS -> 1
        CONFIG -> 2
        LOGS -> 3
        else -> null
    }

    /** Route for a bottom-bar index, or null when the index is out of range. */
    fun routeForTabIndex(index: Int): String? = all.getOrNull(index)
}
