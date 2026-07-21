package com.uplink.ui.detail

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

data class SignalDetailUiState(
    val loading: Boolean = true,
    val signal: Signal? = null,
    val error: String? = null
)

@HiltViewModel
class SignalDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: YouTubioRepository
) : ViewModel() {

    // Reads Routes.SignalDetail.ARG_SIGNAL_ID ("signalId"), matching
    // the real nav graph argument name exactly (verified against
    // UplinkNavGraph.kt / Routes.kt, not assumed).
    private val signalId: String = checkNotNull(
        savedStateHandle[Routes.SignalDetail.ARG_SIGNAL_ID]
    ) { "SignalDetailViewModel requires a non-null signalId nav argument" }

    private val _uiState = MutableStateFlow(SignalDetailUiState())
    val uiState: StateFlow<SignalDetailUiState> = _uiState.asStateFlow()

    init {
        loadSignal()
    }

    fun loadSignal() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)

            when (val result = repository.getSignal(signalId)) {
                is SignalResult.Success -> {
                    _uiState.value = SignalDetailUiState(
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

    fun retry() = loadSignal()
}

private fun <T> SignalResult<T>.toDisplayMessage(): String = when (this) {
    is SignalResult.NetworkError -> "LINK UNREACHABLE: ${exception.message ?: "unknown error"}"
    is SignalResult.BackendError -> "BACKEND FAULT (${code}): ${message ?: "unknown error"}"
    is SignalResult.NotFound -> "SIGNAL NOT FOUND"
    is SignalResult.Success -> ""
}
