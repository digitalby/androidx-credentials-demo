package info.yuryv.androidx_credentials_demo.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import info.yuryv.androidx_credentials_demo.data.CredentialRepository
import info.yuryv.androidx_credentials_demo.util.MockPasskeyData
import info.yuryv.androidx_credentials_demo.util.toDisplayMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PasskeyUiState(
    val isLoading: Boolean = false,
    val registrationResult: String? = null,
    val authenticationResult: String? = null,
    val errorMessage: String? = null,
)

class PasskeyViewModel(
    private val repository: CredentialRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PasskeyUiState())
    val uiState: StateFlow<PasskeyUiState> = _uiState.asStateFlow()

    fun registerPasskey(activity: Activity) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, registrationResult = null) }
            repository
                .createPasskey(activity, MockPasskeyData.registrationRequest)
                .onSuccess { json ->
                    _uiState.update { it.copy(isLoading = false, registrationResult = json) }
                }.onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.toDisplayMessage()) }
                }
        }
    }

    fun authenticateWithPasskey(activity: Activity) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, authenticationResult = null) }
            repository
                .getPasskey(activity, MockPasskeyData.authenticationRequest)
                .onSuccess { json ->
                    _uiState.update { it.copy(isLoading = false, authenticationResult = json) }
                }.onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.toDisplayMessage()) }
                }
        }
    }

    fun clearError() = _uiState.update { it.copy(errorMessage = null) }

    companion object {
        fun factory(repository: CredentialRepository): ViewModelProvider.Factory =
            viewModelFactory {
                initializer { PasskeyViewModel(repository) }
            }
    }
}
