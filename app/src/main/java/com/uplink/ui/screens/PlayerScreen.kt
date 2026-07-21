package com.uplink.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uplink.data.mock.MockSignalRepository
import com.uplink.domain.model.Signal
import com.uplink.domain.result.SignalResult
import com.uplink.ui.components.HeaderStatus
import com.uplink.ui.components.SignalStatusLabel
import com.uplink.ui.components.TerminalHeader
import com.uplink.ui.theme.TelemetryBody
import com.uplink.ui.theme.TerminalTitleMedium
import com.uplink.ui.theme.UplinkAlert
import com.uplink.ui.theme.UplinkLine
import com.uplink.ui.theme.UplinkSignal
import com.uplink.ui.theme.UplinkText
import com.uplink.ui.theme.UplinkTextDim
import com.uplink.ui.theme.UplinkVoid

// Placeholder shell only, per finalized scope — no real playback until
// Commit 005 (Media3 ExoPlayer). This screen exists so the nav graph
// is complete and PLAYER_READY has somewhere real to render once wired.
@Composable
fun PlayerScreen(
    signalId: String,
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
        TerminalHeader(title = "PLAYER_READY", status = HeaderStatus.STABLE)

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
                    Text(text = currentSignal.title, style = TerminalTitleMedium, color = UplinkText)
                    SignalStatusLabel(
                        status = currentSignal.status,
                        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .background(UplinkLine),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "[ VIDEO SURFACE ]",
                            style = TelemetryBody,
                            color = UplinkTextDim
                        )
                    }

                    Text(
                        text = "[ || < > ]  controls placeholder — Commit 005",
                        style = TelemetryBody,
                        color = UplinkSignal,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
    }
}
