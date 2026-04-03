package info.yuryv.androidx_credentials_demo.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import info.yuryv.androidx_credentials_demo.data.CredentialRepository
import info.yuryv.androidx_credentials_demo.util.toDisplayMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PasswordUiState(
    val isLoading: Boolean = false,
    val username: String = "",
    val password: String = "",
    /**
     * Retrieved credentials are shown for demo purposes only.
     * In production, never store or display plaintext passwords beyond their immediate use.
     */
    val retrievedUsername: String? = null,
    val retrievedPassword: String? = null,
    val saveSuccess: Boolean = false,
    val errorMessage: String? = null,
)

class PasswordViewModel(private val repository: CredentialRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(PasswordUiState())
    val uiState: StateFlow<PasswordUiState> = _uiState.asStateFlow()

    fun onUsernameChange(value: String) = _uiState.update { it.copy(username = value) }
    fun onPasswordChange(value: String) = _uiState.update { it.copy(password = value) }

    fun savePassword(activity: Activity) {
        val state = _uiState.value
        if (state.username.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Username and password are required.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, saveSuccess = false) }
            repository.savePassword(activity, state.username, state.password)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, saveSuccess = true) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.toDisplayMessage()) }
                }
        }
    }

    fun getSavedPassword(activity: Activity) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null, retrievedUsername = null, retrievedPassword = null)
            }
            repository.getSavedPassword(activity)
                .onSuccess { credential ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            retrievedUsername = credential.id,
                            retrievedPassword = credential.password,
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.toDisplayMessage()) }
                }
        }
    }

    fun clearError() = _uiState.update { it.copy(errorMessage = null) }
    fun clearSaveSuccess() = _uiState.update { it.copy(saveSuccess = false) }
    fun clearRetrievedCredentials() =
        _uiState.update { it.copy(retrievedUsername = null, retrievedPassword = null) }

    companion object {
        fun factory(repository: CredentialRepository): ViewModelProvider.Factory =
            viewModelFactory {
                initializer { PasswordViewModel(repository) }
            }
    }
}
