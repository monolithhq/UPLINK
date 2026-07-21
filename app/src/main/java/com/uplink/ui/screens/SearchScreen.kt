package com.uplink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.uplink.data.mock.MockSignalRepository
import com.uplink.domain.model.Signal
import com.uplink.domain.result.SignalResult
import com.uplink.ui.components.HeaderStatus
import com.uplink.ui.components.SignalGrid
import com.uplink.ui.components.TerminalHeader
import com.uplink.ui.theme.TelemetryBody
import com.uplink.ui.theme.UplinkAlert
import com.uplink.ui.theme.UplinkLine
import com.uplink.ui.theme.UplinkText
import com.uplink.ui.theme.UplinkTextDim
import com.uplink.ui.theme.UplinkVoid

// Query interface — Commit 003 filters MockSignalRepository in-memory;
// Commit 004 swaps to GET /search?q=. Same repository-shape contract
// as HomeScreen.
@Composable
fun SearchScreen(
    onSignalClick: (Signal) -> Unit,
    repository: MockSignalRepository = remember { MockSignalRepository() }
) {
    var query by remember { mutableStateOf("") }
    var results by remember { mutableStateOf<List<Signal>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(query) {
        if (query.isBlank()) {
            results = emptyList()
            errorMessage = null
            return@LaunchedEffect
        }
        when (val result = repository.search(query)) {
            is SignalResult.Success -> {
                results = result.data
                errorMessage = null
            }
            is SignalResult.NetworkError -> errorMessage = "LINK UNREACHABLE: ${result.exception.message ?: "unknown error"}"
            is SignalResult.BackendError -> errorMessage = "BACKEND FAULT (${result.code}): ${result.message}"
            is SignalResult.NotFound -> errorMessage = "NO MATCH"
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(UplinkVoid)) {
        TerminalHeader(title = "SEARCH_SIGNAL", status = HeaderStatus.STABLE)

        BasicTextField(
            value = query,
            onValueChange = { query = it },
            textStyle = TextStyle(color = UplinkText, fontSize = TelemetryBody.fontSize),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .background(UplinkLine)
                .padding(12.dp)
        )

        when {
            errorMessage != null -> {
                Text(
                    text = errorMessage ?: "",
                    style = TelemetryBody,
                    color = UplinkAlert,
                    modifier = Modifier.weight(1f).padding(16.dp)
                )
            }
            query.isBlank() -> {
                Text(
                    text = "AWAITING INPUT",
                    style = TelemetryBody,
                    color = UplinkTextDim,
                    modifier = Modifier.weight(1f).padding(16.dp)
                )
            }
            results.isEmpty() -> {
                Text(
                    text = "NO SIGNALS MATCHED",
                    style = TelemetryBody,
                    color = UplinkTextDim,
                    modifier = Modifier.weight(1f).padding(16.dp)
                )
            }
            else -> {
                SignalGrid(
                    signals = results,
                    onSignalClick = onSignalClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
