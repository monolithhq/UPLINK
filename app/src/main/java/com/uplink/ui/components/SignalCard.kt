package com.uplink.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uplink.domain.model.Signal
import com.uplink.ui.theme.TelemetryBody
import com.uplink.ui.theme.TerminalTitleMedium
import com.uplink.ui.theme.UplinkLine
import com.uplink.ui.theme.UplinkPanel
import com.uplink.ui.theme.UplinkText
import com.uplink.ui.theme.UplinkTextDim

// Home/Search/Library shared card. Tapping navigates to Signal Detail —
// SignalCard itself has no knowledge of navigation, just reports taps.
// CornerBracketFrame is overlaid only while pressed, per its documented
// intent (focus indicator, not permanent decoration).
@Composable
fun SignalCard(
    signal: Signal,
    onClick: (Signal) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(interactionSource = interactionSource, indication = null) {
                onClick(signal)
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(UplinkPanel, RoundedCornerShape(4.dp))
                .border(1.dp, UplinkLine, RoundedCornerShape(4.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .background(UplinkLine)
            )

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = signal.title,
                    style = TerminalTitleMedium,
                    color = UplinkText,
                    maxLines = 2
                )
                Text(
                    text = signal.channel,
                    style = TelemetryBody,
                    color = UplinkTextDim
                )
                signal.durationSeconds?.let { seconds ->
                    Text(
                        text = formatDuration(seconds),
                        style = TelemetryBody,
                        color = UplinkTextDim
                    )
                }
                SignalStatusLabel(
                    status = signal.status,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        CornerBracketFrame(
            modifier = Modifier.fillMaxWidth().aspectRatio(1.6f),
            visible = isPressed
        )
    }
}

private fun formatDuration(totalSeconds: Long): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}
