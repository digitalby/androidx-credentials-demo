package info.yuryv.androidx_credentials_demo.data

import android.app.Activity
import android.content.Context
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CreatePublicKeyCredentialRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPasswordOption
import androidx.credentials.GetPublicKeyCredentialOption
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.exceptions.NoCredentialException
import androidx.credentials.exceptions.publickeycredential.CreatePublicKeyCredentialDomException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

/**
 * Single source-of-truth for all Credential Manager interactions.
 *
 * The [Activity] reference is accepted per-call rather than stored in the constructor
 * to avoid Activity leaks. All functions are suspending and return [Result] so callers
 * handle success/failure without try/catch at the call site.
 *
 * Exception interpretation (user-readable messages) is the responsibility of ViewModels;
 * this class surfaces exceptions transparently via [Result.failure].
 */
class CredentialRepository(appContext: Context) {

    private val credentialManager = CredentialManager.create(appContext)

    /**
     * Initiates passkey registration. The [requestJson] must be a valid
     * PublicKeyCredentialCreationOptions JSON object (typically from your server).
     *
     * Returns the raw registration response JSON on success.
     *
     * In demo mode, expect [CreatePublicKeyCredentialDomException] because the RP ID
     * is not linked to the app via Digital Asset Links.
     */
    suspend fun createPasskey(
        activity: Activity,
        requestJson: String,
    ): Result<String> = runCatching {
        val request = CreatePublicKeyCredentialRequest(requestJson)
        val response = credentialManager.createCredential(activity, request)
        (response as PublicKeyCredential).authenticationResponseJson
    }

    /**
     * Initiates passkey authentication. The [requestJson] must be a valid
     * PublicKeyCredentialRequestOptions JSON object (typically from your server).
     *
     * Returns the raw authentication assertion JSON on success.
     */
    suspend fun getPasskey(
        activity: Activity,
        requestJson: String,
    ): Result<String> = runCatching {
        val option = GetPublicKeyCredentialOption(requestJson)
        val request = GetCredentialRequest(listOf(option))
        val response = credentialManager.getCredential(activity, request)
        (response.credential as PublicKeyCredential).authenticationResponseJson
    }

    /**
     * Saves a username/password pair via the Credential Manager.
     * The system prompts the user to save via their active autofill provider.
     */
    suspend fun savePassword(
        activity: Activity,
        username: String,
        password: String,
    ): Result<Unit> = runCatching {
        val request = CreatePasswordRequest(username, password)
        credentialManager.createCredential(activity, request)
        Unit
    }

    /**
     * Retrieves a saved password credential from the user's autofill provider.
     *
     * Returns [NoCredentialException] wrapped in [Result.failure] when no credentials
     * are saved — common on emulators without a configured password manager.
     */
    suspend fun getSavedPassword(activity: Activity): Result<PasswordCredential> =
        runCatching {
            val option = GetPasswordOption()
            val request = GetCredentialRequest(listOf(option))
            val response = credentialManager.getCredential(activity, request)
            response.credential as PasswordCredential
        }

    /**
     * Initiates Google Sign-In via the Credential Manager.
     *
     * [webClientId] must be a valid OAuth 2.0 Web client ID from the Google Cloud Console.
     * Supply a [nonce] in production to prevent replay attacks.
     *
     * Returns a [GoogleIdTokenCredential] on success.
     */
    suspend fun signInWithGoogle(
        activity: Activity,
        webClientId: String,
        nonce: String? = null,
    ): Result<GoogleIdTokenCredential> = runCatching {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(webClientId)
            .setNonce(nonce)
            .build()
        val request = GetCredentialRequest(listOf(googleIdOption))
        val response = credentialManager.getCredential(activity, request)
        GoogleIdTokenCredential.createFrom(response.credential.data)
    }

    /**
     * Clears the credential state (sign-out). Call this before allowing the user to
     * sign in with a different Google account; otherwise the picker shows only the
     * previously authorized account.
     */
    suspend fun clearCredentialState(): Result<Unit> = runCatching {
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
    }
}
