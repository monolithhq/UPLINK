package com.uplink.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import com.uplink.ui.screens.DebugConsoleScreen
import com.uplink.ui.screens.HomeScreen
import com.uplink.ui.screens.LibraryScreen
import com.uplink.ui.screens.PlayerScreen
import com.uplink.ui.screens.SearchScreen
import com.uplink.ui.screens.SignalDetailScreen
import com.uplink.ui.theme.TelemetryStatus
import com.uplink.ui.theme.UplinkPanel
import com.uplink.ui.theme.UplinkSignal
import com.uplink.ui.theme.UplinkTextDim
import com.uplink.ui.theme.UplinkVoid

// Per finalized nav flow: Home/Search/Library are the tab-level entry
// points (bottom nav). Signal Detail, Player, and Debug Console are
// push destinations reached from those tabs (or from any screen, in
// Debug Console's case) — they intentionally have no bottom nav tab
// of their own.
private data class BottomTab(val route: String, val label: String)

private val bottomTabs = listOf(
    BottomTab(Routes.Home.route, "HOME"),
    BottomTab(Routes.Search.route, "SEARCH"),
    BottomTab(Routes.Library.route, "LIBRARY")
)

@Composable
fun UplinkNavGraph() {
    val navController: NavHostController = rememberNavController()

    Column(modifier = Modifier.fillMaxSize().background(UplinkVoid)) {
        NavHost(
            navController = navController,
            startDestination = Routes.Home.route,
            modifier = Modifier.weight(1f)
        ) {
            composable(Routes.Home.route) {
                HomeScreen(
                    onSignalClick = { signal ->
                        navController.navigate(Routes.SignalDetail.createRoute(signal.id))
                    }
                )
            }

            composable(Routes.Search.route) {
                SearchScreen(
                    onSignalClick = { signal ->
                        navController.navigate(Routes.SignalDetail.createRoute(signal.id))
                    }
                )
            }

            composable(Routes.Library.route) {
                LibraryScreen(
                    onSignalClick = { signal ->
                        navController.navigate(Routes.SignalDetail.createRoute(signal.id))
                    }
                )
            }

            composable(Routes.SignalDetail.route) { backStackEntry ->
                val signalId = backStackEntry.arguments
                    ?.getString(Routes.SignalDetail.ARG_SIGNAL_ID)
                    ?: return@composable
                SignalDetailScreen(
                    signalId = signalId,
                    onPlayClick = { signal ->
                        navController.navigate(Routes.Player.createRoute(signal.id))
                    }
                )
            }

            composable(Routes.Player.route) { backStackEntry ->
                val signalId = backStackEntry.arguments
                    ?.getString(Routes.Player.ARG_SIGNAL_ID)
                    ?: return@composable
                PlayerScreen(signalId = signalId)
            }

            composable(Routes.DebugConsole.route) {
                DebugConsoleScreen()
            }
        }

        BottomTerminalNav(
            navController = navController,
            onDebugConsoleClick = { navController.navigate(Routes.DebugConsole.route) }
        )
    }
}

// Bottom nav bar in the terminal aesthetic — text labels, no icon set
// defined yet (per handoff's open gaps list), plus a persistent debug
// console entry point available from any tab.
@Composable
private fun BottomTerminalNav(
    navController: NavHostController,
    onDebugConsoleClick: () -> Unit
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(UplinkPanel)
            .padding(vertical = 12.dp)
    ) {
        androidx.compose.foundation.layout.Row(modifier = Modifier.fillMaxWidth()) {
            bottomTabs.forEach { tab ->
                val isSelected = currentDestination?.hierarchy?.any { it.route == tab.route } == true
                Text(
                    text = tab.label,
                    style = TelemetryStatus,
                    color = if (isSelected) UplinkSignal else UplinkTextDim,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                )
            }
            Text(
                text = "DEBUG",
                style = TelemetryStatus,
                color = if (currentDestination?.route == Routes.DebugConsole.route) UplinkSignal else UplinkTextDim,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onDebugConsoleClick() }
            )
        }
    }
}
