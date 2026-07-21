package com.uplink.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uplink.ui.theme.TelemetryBody
import com.uplink.ui.theme.TelemetryStatus
import com.uplink.ui.theme.UplinkSignal
import com.uplink.ui.theme.UplinkTextDim

// ASCII-terminal-style signal acquisition beat, matching UPLINK's
// existing design language (terminal punctuation, mono font, telemetry
// voice) rather than a generic bar meter.
//
// Fixed ~300ms UI transition beat, not a subsystem wait — matches the
// design brief's "brief signal acquisition moment" duration.
@Composable
fun SignalLockAnimation(
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var started by remember { mutableStateOf(false) }

    val progress by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(durationMillis = 300, easing = LinearEasing),
        label = "signal_lock_fill",
        finishedListener = { onComplete() }
    )

    LaunchedEffect(Unit) {
        started = true
    }

    val barWidth = 20
    val filledCount = (barWidth * progress).toInt()
    val bar = "█".repeat(filledCount) + " ".repeat(barWidth - filledCount)
    val percent = (progress * 98.3f)

    Column(modifier = modifier.padding(24.dp)) {
        Text(
            text = "SIGNAL_ACQUISITION",
            style = TelemetryStatus,
            color = UplinkTextDim
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "[$bar]",
            style = TelemetryBody,
            color = UplinkSignal
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = if (progress >= 1f) "SIGNAL_LOCKED" else "%.1f%%".format(percent),
            style = TelemetryBody,
            color = if (progress >= 1f) UplinkSignal else UplinkTextDim
        )
    }
}
