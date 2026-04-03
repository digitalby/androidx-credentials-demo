package info.yuryv.androidx_credentials_demo

import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialInterruptedException
import androidx.credentials.exceptions.GetCredentialUnsupportedException
import androidx.credentials.exceptions.NoCredentialException
import info.yuryv.androidx_credentials_demo.util.toDisplayMessage
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Verifies that [toDisplayMessage] maps known Credential Manager exceptions to
 * recognizable, non-empty strings. These are unit tests — no Android instrumentation required.
 */
class CredentialErrorTest {

    @Test
    fun `NoCredentialException maps to no-saved-credentials message`() {
        val msg = NoCredentialException().toDisplayMessage()
        assertTrue("Expected message about no saved credentials, got: $msg",
            msg.contains("No saved credentials", ignoreCase = true))
    }

    @Test
    fun `GetCredentialCancellationException maps to cancelled message`() {
        val msg = GetCredentialCancellationException().toDisplayMessage()
        assertTrue("Expected cancellation message, got: $msg",
            msg.contains("Cancelled", ignoreCase = true))
    }

    @Test
    fun `GetCredentialInterruptedException maps to interrupted message`() {
        val msg = GetCredentialInterruptedException().toDisplayMessage()
        assertTrue("Expected interrupted message, got: $msg",
            msg.contains("Interrupted", ignoreCase = true))
    }

    @Test
    fun `GetCredentialUnsupportedException maps to unsupported message`() {
        val msg = GetCredentialUnsupportedException().toDisplayMessage()
        assertTrue("Expected unsupported message, got: $msg",
            msg.contains("not supported", ignoreCase = true))
    }

    @Test
    fun `unknown exception falls back to message or class name`() {
        val ex = RuntimeException("something broke")
        val msg = ex.toDisplayMessage()
        assertTrue("Expected fallback to contain exception message, got: $msg",
            msg.contains("something broke"))
    }

    @Test
    fun `toDisplayMessage never returns blank`() {
        val exceptions = listOf(
            NoCredentialException(),
            GetCredentialCancellationException(),
            GetCredentialInterruptedException(),
            GetCredentialUnsupportedException(),
            RuntimeException(),
        )
        for (ex in exceptions) {
            val msg = ex.toDisplayMessage()
            assertTrue("Expected non-blank message for ${ex::class.simpleName}, got: \"$msg\"",
                msg.isNotBlank())
        }
    }
}
