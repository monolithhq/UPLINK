package com.uplink.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.uplink.domain.model.SignalStatus
import com.uplink.domain.model.displayLabel
import com.uplink.ui.theme.TelemetryStatus
import com.uplink.ui.theme.UplinkAlert
import com.uplink.ui.theme.UplinkAmber
import com.uplink.ui.theme.UplinkSignal
import com.uplink.ui.theme.UplinkTextDim

// Renders a Signal's user-facing status label ("SIGNAL LOCKED", "ON AIR",
// etc — never the raw enum name; see Signal.displayLabel()). Color-codes
// by state so the terminal aesthetic carries meaning, not just decoration.
@Composable
fun SignalStatusLabel(
    status: SignalStatus,
    modifier: Modifier = Modifier
) {
    val color = when (status) {
        SignalStatus.DISCOVERED -> UplinkTextDim
        SignalStatus.RESOLVING -> UplinkAmber
        SignalStatus.READY -> UplinkSignal
        SignalStatus.PLAYING -> UplinkSignal
        SignalStatus.UNAVAILABLE -> UplinkAlert
        SignalStatus.ERROR -> UplinkAlert
    }

    Text(
        text = "STATUS: ${status.displayLabel()}",
        style = TelemetryStatus,
        color = color,
        modifier = modifier
    )
}
