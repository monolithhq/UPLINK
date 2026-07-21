package com.uplink.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uplink.data.database.PreferencesDao
import com.uplink.data.preferences.PreferenceKeys
import com.uplink.data.repository.YouTubioRepository
import com.uplink.domain.result.SignalResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val currentUrl: String = "",
    val currentConfig: String = "",
    val configured: Boolean = false,
    val saving: Boolean = false,
    val testing: Boolean = false,
    val testResult: String? = null,
    val error: String? = null
)

// NOTE: YouTubioRepository currently exposes no public getter for the
// persisted URL/config (only its private requireConfig() reads them
// internally) — see YouTubioRepository.kt. Rather than modify the
// repository in this step, this ViewModel injects PreferencesDao
// directly (already provided as @Singleton via DatabaseModule) to read
// current values. If a public repository getter is added later, this
// can switch to it without changing this class's public API.
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: YouTubioRepository,
    private val preferencesDao: PreferencesDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadCurrentSettings()
    }

    fun loadCurrentSettings() {
        viewModelScope.launch {
            val url = preferencesDao.getValue(PreferenceKeys.YOUTUBIO_URL).orEmpty()
            val config = preferencesDao.getValue(PreferenceKeys.YOUTUBIO_CONFIG).orEmpty()
            val configured = repository.isConfigured()

            _uiState.value = _uiState.value.copy(
                currentUrl = url,
                currentConfig = config,
                configured = configured
            )
        }
    }

    fun saveYouTubioUrl(url: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(saving = true, error = null)
            repository.saveYouTubioUrl(url)
            loadCurrentSettings()
            _uiState.value = _uiState.value.copy(saving = false)
        }
    }

    fun saveYouTubioConfig(config: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(saving = true, error = null)
            repository.saveYouTubioConfig(config)
            loadCurrentSettings()
            _uiState.value = _uiState.value.copy(saving = false)
        }
    }

    fun testConnection() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(testing = true, testResult = null, error = null)

            when (val result = repository.testConnection()) {
                is SignalResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        testing = false,
                        testResult = "CONNECTION OK"
                    )
                }
                else -> {
                    _uiState.value = _uiState.value.copy(
                        testing = false,
                        testResult = null,
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
