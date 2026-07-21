package com.uplink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.weight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.uplink.data.mock.MockSignalRepository
import com.uplink.domain.model.Signal
import com.uplink.domain.result.SignalResult
import com.uplink.ui.components.HeaderStatus
import com.uplink.ui.components.SignalGrid
import com.uplink.ui.components.TerminalHeader
import com.uplink.ui.theme.TelemetryBody
import com.uplink.ui.theme.UplinkAlert
import com.uplink.ui.theme.UplinkTextDim
import com.uplink.ui.theme.UplinkVoid

// "SAVED SIGNALS" — Commit 003 reads a mock subset from
// MockSignalRepository; Commit 004+ backs this with Room (history,
// bookmarks, offline cache per UPLINK_DATABASE_SCHEMA.md). Same
// repository-shape contract as Home/Search.
@Composable
fun LibraryScreen(
    onSignalClick: (Signal) -> Unit,
    repository: MockSignalRepository = remember { MockSignalRepository() }
) {
    var signals by remember { mutableStateOf<List<Signal>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading = true
        when (val result = repository.getLibrary()) {
            is SignalResult.Success -> {
                signals = result.data
                errorMessage = null
            }
            is SignalResult.NetworkError -> errorMessage = "LINK UNREACHABLE: ${result.message}"
            is SignalResult.BackendError -> errorMessage = "BACKEND FAULT (${result.code}): ${result.message}"
            is SignalResult.NotFound -> errorMessage = "LIBRARY NOT FOUND"
        }
        isLoading = false
    }

    Column(modifier = Modifier.fillMaxSize().background(UplinkVoid)) {
        TerminalHeader(title = "SAVED_SIGNALS", status = HeaderStatus.STABLE)

        when {
            errorMessage != null -> {
                Text(
                    text = errorMessage ?: "",
                    style = TelemetryBody,
                    color = UplinkAlert,
                    modifier = Modifier.weight(1f).background(UplinkVoid)
                )
            }
            isLoading -> {
                Text(
                    text = "LOADING ARCHIVE...",
                    style = TelemetryBody,
                    color = UplinkTextDim,
                    modifier = Modifier.weight(1f)
                )
            }
            signals.isEmpty() -> {
                Text(
                    text = "NO SAVED SIGNALS",
                    style = TelemetryBody,
                    color = UplinkTextDim,
                    modifier = Modifier.weight(1f)
                )
            }
            else -> {
                SignalGrid(
                    signals = signals,
                    onSignalClick = onSignalClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
