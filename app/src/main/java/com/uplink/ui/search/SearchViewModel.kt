package com.uplink.ui.search

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

data class SearchUiState(
    val query: String = "",
    val results: List<Signal> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: YouTubioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    // Repository.search() reads the local Room cache (no live YouTubio
    // search endpoint exists yet — see YouTubioRepository), so this is
    // intentionally not debounced/throttled the way a network-search
    // field usually would be. Revisit if search moves off local cache.
    fun search(query: String) {
        _uiState.value = _uiState.value.copy(query = query)

        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(results = emptyList(), loading = false, error = null)
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)

            when (val result = repository.search(query)) {
                is SignalResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        results = result.data,
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
}

private fun <T> SignalResult<T>.toDisplayMessage(): String = when (this) {
    is SignalResult.NetworkError -> "LINK UNREACHABLE: ${exception.message ?: "unknown error"}"
    is SignalResult.BackendError -> "BACKEND FAULT (${code}): ${message ?: "unknown error"}"
    is SignalResult.NotFound -> "SIGNAL NOT FOUND"
    is SignalResult.Success -> ""
}
