package com.uplink.ui.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uplink.data.repository.YouTubioRepository
import com.uplink.domain.model.Signal
import com.uplink.domain.result.SignalResult
import com.uplink.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlayerUiState(
    val loading: Boolean = true,
    val signal: Signal? = null,
    val error: String? = null
)

@HiltViewModel
class PlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: YouTubioRepository
) : ViewModel() {

    private val signalId: String = checkNotNull(
        savedStateHandle[Routes.Player.ARG_SIGNAL_ID]
    ) { "PlayerViewModel requires a non-null signalId nav argument" }

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    init {
        resolveStream()
    }

    // Deliberately calls resolveStream(), not getSignal(). Player needs
    // Signal.streamUrl populated, which only resolveStream() guarantees
    // — getSignal() only loads/enriches metadata. See
    // YouTubioRepository.resolveStream() for the cache-then-resolve
    // flow this depends on.
    fun resolveStream() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)

            when (val result = repository.resolveStream(signalId)) {
                is SignalResult.Success -> {
                    _uiState.value = PlayerUiState(
                        loading = false,
                        signal = result.data,
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

    fun retry() = resolveStream()
}

private fun <T> SignalResult<T>.toDisplayMessage(): String = when (this) {
    is SignalResult.NetworkError -> "LINK UNREACHABLE: ${exception.message ?: "unknown error"}"
    is SignalResult.BackendError -> "BACKEND FAULT (${code}): ${message ?: "unknown error"}"
    is SignalResult.NotFound -> "SIGNAL NOT FOUND"
    is SignalResult.Success -> ""
}
