package com.example.lxb_ignition.ui.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TopNavRoutesTest {

    @Test
    fun all_containsFourDistinctRoutesWithControlFirst() {
        assertEquals(listOf("control", "tasks", "config", "logs"), TopNavRoutes.all)
        assertEquals(4, TopNavRoutes.all.toSet().size)
        assertEquals(TopNavRoutes.CONTROL, TopNavRoutes.all.first())
    }

    @Test
    fun tabIndexForRoute_mapsEveryRouteAndRejectsUnknown() {
        assertEquals(0, TopNavRoutes.tabIndexForRoute(TopNavRoutes.CONTROL))
        assertEquals(1, TopNavRoutes.tabIndexForRoute(TopNavRoutes.TASKS))
        assertEquals(2, TopNavRoutes.tabIndexForRoute(TopNavRoutes.CONFIG))
        assertEquals(3, TopNavRoutes.tabIndexForRoute(TopNavRoutes.LOGS))
        assertNull(TopNavRoutes.tabIndexForRoute("settings"))
        assertNull(TopNavRoutes.tabIndexForRoute(""))
        assertNull(TopNavRoutes.tabIndexForRoute(null))
    }

    @Test
    fun routeForTabIndex_mapsEveryIndexAndRejectsOutOfRange() {
        assertEquals("control", TopNavRoutes.routeForTabIndex(0))
        assertEquals("tasks", TopNavRoutes.routeForTabIndex(1))
        assertEquals("config", TopNavRoutes.routeForTabIndex(2))
        assertEquals("logs", TopNavRoutes.routeForTabIndex(3))
        assertNull(TopNavRoutes.routeForTabIndex(-1))
        assertNull(TopNavRoutes.routeForTabIndex(4))
    }

    @Test
    fun mappings_roundTrip() {
        for (route in TopNavRoutes.all) {
            val index = TopNavRoutes.tabIndexForRoute(route)
            assertEquals(route, TopNavRoutes.routeForTabIndex(index!!))
        }
        for (index in TopNavRoutes.all.indices) {
            val route = TopNavRoutes.routeForTabIndex(index)
            assertEquals(index, TopNavRoutes.tabIndexForRoute(route))
        }
    }
}
