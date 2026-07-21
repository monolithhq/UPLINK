package com.uplink.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uplink.domain.model.Signal

// Single-column list of SignalCards. Named "Grid" per the original
// component spec, but Commit 003 ships a single-column layout to match
// the terminal/console aesthetic (cards read like stacked log entries
// rather than a media-app grid) — can become a real grid later without
// changing the call sites, since callers only pass a list + click handler.
@Composable
fun SignalGrid(
    signals: List<Signal>,
    onSignalClick: (Signal) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp)
    ) {
        items(signals, key = { it.id }) { signal ->
            SignalCard(
                signal = signal,
                onClick = onSignalClick,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
    }
}
