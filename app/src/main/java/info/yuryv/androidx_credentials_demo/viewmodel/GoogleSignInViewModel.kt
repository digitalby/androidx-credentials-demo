package info.yuryv.androidx_credentials_demo.viewmodel

import android.app.Activity
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import info.yuryv.androidx_credentials_demo.BuildConfig
import info.yuryv.androidx_credentials_demo.data.CredentialRepository
import info.yuryv.androidx_credentials_demo.util.toDisplayMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GoogleSignInUiState(
    val isLoading: Boolean = false,
    val userDisplayName: String? = null,
    val userEmail: String? = null,
    val isSignedIn: Boolean = false,
    val isClientIdMissing: Boolean = false,
    val errorMessage: String? = null,
)

class GoogleSignInViewModel(private val repository: CredentialRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(
        GoogleSignInUiState(
            isClientIdMissing = BuildConfig.GOOGLE_WEB_CLIENT_ID.contains("YOUR_WEB_CLIENT"),
        ),
    )
    val uiState: StateFlow<GoogleSignInUiState> = _uiState.asStateFlow()

    fun signInWithGoogle(activity: Activity) {
        if (_uiState.value.isClientIdMissing) {
            _uiState.update {
                it.copy(
                    errorMessage = "Set a real Web client ID in app/build.gradle.kts " +
                        "under buildConfigField(\"GOOGLE_WEB_CLIENT_ID\").",
                )
            }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.signInWithGoogle(activity, BuildConfig.GOOGLE_WEB_CLIENT_ID)
                .onSuccess { credential ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSignedIn = true,
                            userDisplayName = credential.displayName,
                            userEmail = credential.id,
                        )
                    }
                }
                .onFailure { error ->
                    if (error is GetCredentialCancellationException) {
                        _uiState.update { it.copy(isLoading = false) }
                    } else {
                        _uiState.update { it.copy(isLoading = false, errorMessage = error.toDisplayMessage()) }
                    }
                }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            repository.clearCredentialState()
            _uiState.update {
                it.copy(
                    isSignedIn = false,
                    userDisplayName = null,
                    userEmail = null,
                    errorMessage = null,
                )
            }
        }
    }

    fun clearError() = _uiState.update { it.copy(errorMessage = null) }

    companion object {
        fun factory(repository: CredentialRepository): ViewModelProvider.Factory =
            viewModelFactory {
                initializer { GoogleSignInViewModel(repository) }
            }
    }
}
