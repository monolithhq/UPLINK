package com.uplink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uplink.ui.components.HeaderStatus
import com.uplink.ui.components.TerminalHeader
import com.uplink.ui.theme.TelemetryBody
import com.uplink.ui.theme.UplinkAlert
import com.uplink.ui.theme.UplinkSignal
import com.uplink.ui.theme.UplinkTextDim
import com.uplink.ui.theme.UplinkVoid

// Routed mock screen for Commit 003. Uses RAW technical event names —
// never the user-facing SignalStatus.displayLabel() mapping — per the
// established layer-separation rule. Real pipeline (Boot Events ->
// DebugEventRepository -> this screen) is deferred to Commit 006.
private data class MockLogLine(val timestamp: String, val event: String, val ok: Boolean)

private val mockLog = listOf(
    MockLogLine("14:03:12", "SIGNAL_DISCOVERED", ok = true),
    MockLogLine("14:03:13", "STREAM_REQUESTED", ok = true),
    MockLogLine("14:03:14", "STREAM_READY", ok = true),
    MockLogLine("14:03:15", "PLAYER_INITIALIZED", ok = true),
    MockLogLine("14:04:02", "STREAM_RESOLUTION_FAILED", ok = false)
)

@Composable
fun DebugConsoleScreen() {
    Column(modifier = Modifier.fillMaxSize().background(UplinkVoid)) {
        TerminalHeader(title = "DEBUG_CONSOLE", status = HeaderStatus.STABLE)

        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            items(mockLog) { line ->
                Text(
                    text = "${line.timestamp}  ${line.event}  ${if (line.ok) "OK" else "ERR"}",
                    style = TelemetryBody,
                    color = if (line.ok) UplinkTextDim else UplinkAlert,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            item {
                Text(
                    text = "-- mock log, real pipeline deferred to Commit 006 --",
                    style = TelemetryBody,
                    color = UplinkSignal,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
    }
}
