package com.uplink.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uplink.data.repository.YouTubioRepository
import com.uplink.domain.model.Signal
import com.uplink.domain.result.SignalResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// UI-facing snapshot of the Home catalog. Compose never sees
// SignalResult directly — errors are pre-mapped to a display string
// here, matching the existing screens' "LINK UNREACHABLE: ..." /
// "BACKEND FAULT (...): ..." conventions so the eventual screen
// migration is a mechanical wiring swap, not a rewrite.
data class HomeUiState(
    val loading: Boolean = true,
    val signals: List<Signal> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: YouTubioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadCatalog()
    }

    fun loadCatalog() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)

            when (val result = repository.getCatalog()) {
                is SignalResult.Success -> {
                    _uiState.value = HomeUiState(
                        loading = false,
                        signals = result.data,
                        error = null
                    )
                }
                else -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        error = result.toDisplayMessage()
                    )
                }
            }
        }
    }

    fun retry() = loadCatalog()
}

// Shared error-mapping convention across all six ViewModels. Kept as a
// private extension per file rather than a shared util class for now —
// each ViewModel's mapping is identical today, but Settings/Player may
// reasonably diverge in wording later without needing to touch a
// shared file. Revisit if duplication becomes a real problem.
private fun <T> SignalResult<T>.toDisplayMessage(): String = when (this) {
    is SignalResult.NetworkError -> "LINK UNREACHABLE: ${exception.message ?: "unknown error"}"
    is SignalResult.BackendError -> "BACKEND FAULT (${code}): ${message ?: "unknown error"}"
    is SignalResult.NotFound -> "SIGNAL NOT FOUND"
    is SignalResult.Success -> "" // unreachable in error-path call sites
}
