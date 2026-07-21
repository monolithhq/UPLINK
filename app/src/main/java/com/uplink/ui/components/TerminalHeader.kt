package com.uplink.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uplink.ui.theme.TelemetryStatus
import com.uplink.ui.theme.TerminalTitleMedium
import com.uplink.ui.theme.UplinkAlert
import com.uplink.ui.theme.UplinkAmber
import com.uplink.ui.theme.UplinkSignal

enum class HeaderStatus {
    STABLE,        // UPLINK_STABLE
    CONNECTING,    // ESTABLISHING_LINK
    FAILED         // SIGNAL_LOST
}

// Global screen identification, used at the top of every screen.
// Per UPLINK_COMPONENT_LIBRARY.md: TerminalHeader(title, status, alert)
@Composable
fun TerminalHeader(
    title: String,
    status: HeaderStatus = HeaderStatus.STABLE,
    alert: String? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth().padding(16.dp)) {
        Text(
            text = "// $title",
            style = TerminalTitleMedium,
            color = com.uplink.ui.theme.UplinkText
        )

        val (statusLabel, statusColor) = when (status) {
            HeaderStatus.STABLE -> "UPLINK_STABLE" to UplinkSignal
            HeaderStatus.CONNECTING -> "ESTABLISHING_LINK" to UplinkAmber
            HeaderStatus.FAILED -> "SIGNAL_LOST" to UplinkAlert
        }

        Text(
            text = alert ?: statusLabel,
            style = TelemetryStatus,
            color = if (alert != null) UplinkAlert else statusColor
        )
    }
}
