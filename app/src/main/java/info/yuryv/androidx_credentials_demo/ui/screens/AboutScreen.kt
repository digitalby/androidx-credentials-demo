package info.yuryv.androidx_credentials_demo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import info.yuryv.androidx_credentials_demo.ui.components.SectionHeader
import info.yuryv.androidx_credentials_demo.util.MockPasskeyData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 16.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                SectionHeader("What is Credential Manager?")
                BodyText(
                    "androidx.credentials is the Android Credential Manager API. It provides a " +
                        "unified, bottom-sheet credential picker that surfaces passkeys, passwords, " +
                        "and federated identity (Google Sign-In) in a single UI. It replaces the " +
                        "legacy FIDO2 API and SmartLock for Passwords.",
                )
            }

            item { HorizontalDivider() }

            item {
                SectionHeader("Passkeys (FIDO2/WebAuthn)")
                BodyText(
                    "Passkeys are phishing-resistant, device-bound credentials based on public-key " +
                        "cryptography. The private key never leaves the device; the public key is " +
                        "registered with your server (Relying Party).\n\n" +
                        "This demo uses hardcoded challenge JSON. A real app must:\n" +
                        "  1. Fetch a server-generated challenge from /auth/register/begin.\n" +
                        "  2. Call CredentialManager.createCredential() with the challenge.\n" +
                        "  3. Send the signed response to /auth/register/complete for verification.\n\n" +
                        "Why does registration fail here?\n" +
                        "The device verifies that the RP ID (\"${MockPasskeyData.RP_ID}\") is " +
                        "associated with this app via Digital Asset Links. Without the file " +
                        "https://${MockPasskeyData.RP_ID}/.well-known/assetlinks.json containing " +
                        "the app's SHA-256 certificate fingerprint and package name, the OS throws " +
                        "CreatePublicKeyCredentialDomException.",
                )
            }

            item { HorizontalDivider() }

            item {
                SectionHeader("Digital Asset Links setup")
                BodyText(
                    "To enable end-to-end passkey flows:\n" +
                        "  1. Host a file at https://<your-domain>/.well-known/assetlinks.json.\n" +
                        "  2. The file must declare relation \"delegate_permission/common.handle_all_urls\" " +
                        "and list your app's package name plus its SHA-256 signing certificate fingerprint.\n" +
                        "  3. Update MockPasskeyData.RP_ID to match your domain.\n" +
                        "  4. Use the same domain in the registration/authentication request JSON's " +
                        "rp.id / rpId field.",
                )
            }

            item { HorizontalDivider() }

            item {
                SectionHeader("Password credentials")
                BodyText(
                    "CredentialManager can save and retrieve username/password pairs via the active " +
                        "autofill provider (Google Password Manager, 1Password, etc.). " +
                        "CreatePasswordRequest triggers a system save prompt; " +
                        "GetPasswordOption triggers a credential picker.\n\n" +
                        "On emulators without a configured autofill provider, " +
                        "getSavedPassword() will throw NoCredentialException — expected.",
                )
            }

            item { HorizontalDivider() }

            item {
                SectionHeader("Google Sign-In")
                BodyText(
                    "GetGoogleIdOption uses the Google Identity Services library to retrieve a " +
                        "Google ID token via the Credential Manager bottom sheet. This replaces the " +
                        "legacy Google Sign-In SDK.\n\n" +
                        "Setup required:\n" +
                        "  1. Create an OAuth 2.0 Web client ID in the Google Cloud Console for your project.\n" +
                        "  2. Replace the placeholder in app/build.gradle.kts:\n" +
                        "     buildConfigField(\"GOOGLE_WEB_CLIENT_ID\", \"<your-client-id>\")\n" +
                        "  3. Optionally supply a nonce to prevent replay attacks in production.",
                )
            }

            item { HorizontalDivider() }

            item {
                SectionHeader("API requirements")
                BodyText(
                    "Minimum SDK: API 28 (Android 9.0).\n" +
                        "Native platform passkey support (no Play Services): API 35+.\n" +
                        "On API 28-34, passkeys rely on the Play Services FIDO2 bridge " +
                        "(credentials-play-services-auth artifact).\n\n" +
                        "Reference: developer.android.com/identity/sign-in/credential-manager",
                )
            }
        }
    }
}

@Composable
private fun BodyText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
