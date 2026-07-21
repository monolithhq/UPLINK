package com.uplink.ui.navigation

// Centralized route definitions. Signal Detail and Player take a
// signal id argument; every other screen is a flat route. Kept as a
// sealed class (not raw strings at call sites) so route construction
// and argument encoding live in exactly one place.
sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Search : Routes("search")
    object Library : Routes("library")
    object DebugConsole : Routes("debug_console")

    object SignalDetail : Routes("signal_detail/{signalId}") {
        fun createRoute(signalId: String) = "signal_detail/$signalId"
        const val ARG_SIGNAL_ID = "signalId"
    }

    object Player : Routes("player/{signalId}") {
        fun createRoute(signalId: String) = "player/$signalId"
        const val ARG_SIGNAL_ID = "signalId"
    }
}
