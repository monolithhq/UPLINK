package com.uplink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.uplink.data.mock.MockSignalRepository
import com.uplink.domain.model.Signal
import com.uplink.domain.result.SignalResult
import com.uplink.ui.components.HeaderStatus
import com.uplink.ui.components.SignalStatusLabel
import com.uplink.ui.components.TerminalHeader
import com.uplink.ui.theme.ContentBody
import com.uplink.ui.theme.TelemetryBody
import com.uplink.ui.theme.TelemetryCaption
import com.uplink.ui.theme.TerminalTitleLarge
import com.uplink.ui.theme.UplinkAlert
import com.uplink.ui.theme.UplinkLine
import com.uplink.ui.theme.UplinkSignal
import com.uplink.ui.theme.UplinkText
import com.uplink.ui.theme.UplinkTextDim
import com.uplink.ui.theme.UplinkVoid

// "SIGNAL INSPECT" — metadata stop between a feed/search/library tap
// and Player. Reachable from Home, Search, and Library per the
// finalized nav flow; onPlayClick is the only way into Player.
@Composable
fun SignalDetailScreen(
    signalId: String,
    onPlayClick: (Signal) -> Unit,
    repository: MockSignalRepository = remember { MockSignalRepository() }
) {
    var signal by remember { mutableStateOf<Signal?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(signalId) {
        when (val result = repository.getSignal(signalId)) {
            is SignalResult.Success -> {
                signal = result.data
                errorMessage = null
            }
            is SignalResult.NotFound -> errorMessage = "SIGNAL NOT FOUND: $signalId"
            is SignalResult.NetworkError -> errorMessage = "LINK UNREACHABLE: ${result.message}"
            is SignalResult.BackendError -> errorMessage = "BACKEND FAULT (${result.code}): ${result.message}"
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(UplinkVoid)) {
        TerminalHeader(title = "SIGNAL_INSPECT", status = HeaderStatus.STABLE)

        val currentSignal = signal
        when {
            errorMessage != null -> {
                Text(
                    text = errorMessage ?: "",
                    style = TelemetryBody,
                    color = UplinkAlert,
                    modifier = Modifier.padding(16.dp)
                )
            }
            currentSignal == null -> {
                Text(
                    text = "RESOLVING...",
                    style = TelemetryBody,
                    color = UplinkTextDim,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else -> {
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .background(UplinkLine)
                    )

                    Text(
                        text = currentSignal.title,
                        style = TerminalTitleLarge,
                        color = UplinkText,
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    SignalStatusLabel(
                        status = currentSignal.status,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    DetailRow(label = "CHANNEL", value = currentSignal.channel)
                    currentSignal.durationSeconds?.let {
                        DetailRow(label = "DURATION", value = formatDuration(it))
                    }
                    DetailRow(label = "SOURCE NODE", value = currentSignal.sourcePlatform.uppercase())
                    currentSignal.viewCount?.let {
                        DetailRow(label = "VIEWS", value = it.toString())
                    }

                    currentSignal.description?.let {
                        Text(
                            text = it,
                            style = ContentBody,
                            color = UplinkTextDim,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }

                    Box(modifier = Modifier.weight(1f))

                    val canPlay = currentSignal.streamUrl != null
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, if (canPlay) UplinkSignal else UplinkLine)
                            .clickable(enabled = canPlay) { onPlayClick(currentSignal) }
                            .padding(16.dp)
                    ) {
                        Text(
                            text = if (canPlay) "[ PLAY SIGNAL ]" else "[ SIGNAL UNAVAILABLE ]",
                            style = TelemetryBody,
                            color = if (canPlay) UplinkSignal else UplinkTextDim
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column(modifier = Modifier.padding(top = 8.dp)) {
        Text(text = label, style = TelemetryCaption, color = UplinkTextDim)
        Text(text = value, style = TelemetryBody, color = UplinkText)
    }
}

private fun formatDuration(totalSeconds: Long): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}
