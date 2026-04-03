package info.yuryv.androidx_credentials_demo.util

import androidx.credentials.exceptions.CreateCredentialCancellationException
import androidx.credentials.exceptions.CreateCredentialInterruptedException
import androidx.credentials.exceptions.CreateCredentialUnsupportedException
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialInterruptedException
import androidx.credentials.exceptions.GetCredentialUnsupportedException
import androidx.credentials.exceptions.NoCredentialException
import androidx.credentials.exceptions.publickeycredential.CreatePublicKeyCredentialDomException

/**
 * Maps Credential Manager exceptions to user-readable display messages.
 * This is intentionally kept in the util layer so ViewModels remain free of
 * raw exception string formatting.
 */
fun Throwable.toDisplayMessage(): String = when (this) {
    is CreatePublicKeyCredentialDomException ->
        "Passkey creation failed: Digital Asset Links not configured for this app. " +
            "Expected in demo mode — see the About screen for setup instructions."

    is NoCredentialException ->
        "No saved credentials found. Save a password first, or sign in with Google."

    is GetCredentialCancellationException,
    is CreateCredentialCancellationException,
    -> "Cancelled."

    is GetCredentialInterruptedException,
    is CreateCredentialInterruptedException,
    -> "Interrupted — please try again."

    is GetCredentialUnsupportedException,
    is CreateCredentialUnsupportedException,
    -> "Credential Manager is not supported on this device or OS version."

    else -> message ?: "An unexpected error occurred (${this::class.simpleName})."
}
