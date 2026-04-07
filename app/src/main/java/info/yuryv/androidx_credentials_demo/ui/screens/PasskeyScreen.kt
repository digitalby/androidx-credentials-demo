package info.yuryv.androidx_credentials_demo.ui.screens

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import info.yuryv.androidx_credentials_demo.ui.components.InfoBanner
import info.yuryv.androidx_credentials_demo.ui.components.ResultDisplay
import info.yuryv.androidx_credentials_demo.ui.components.SectionHeader
import info.yuryv.androidx_credentials_demo.util.MockPasskeyData
import info.yuryv.androidx_credentials_demo.viewmodel.PasskeyUiState
import info.yuryv.androidx_credentials_demo.viewmodel.PasskeyViewModel

@Composable
fun PasskeyScreen(
    viewModel: PasskeyViewModel,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val activity = LocalActivity.current
    PasskeyScreenContent(
        uiState = uiState,
        onBack = onBack,
        onRegister = { activity?.let { viewModel.registerPasskey(activity) } },
        onAuthenticate = { activity?.let { viewModel.authenticateWithPasskey(activity) } },
        onClearError = viewModel::clearError,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasskeyScreenContent(
    uiState: PasskeyUiState,
    onBack: () -> Unit,
    onRegister: () -> Unit,
    onAuthenticate: () -> Unit,
    onClearError: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        val msg = uiState.errorMessage ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(msg)
        onClearError()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Passkeys") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            InfoBanner(
                message = "Demo mode: uses hardcoded challenge data. Passkey creation requires " +
                    "Digital Asset Links at https://${MockPasskeyData.RP_ID}/.well-known/assetlinks.json " +
                    "linking package info.yuryv.androidx_credentials_demo with your app's signing certificate. " +
                    "Expect CreatePublicKeyCredentialDomException without this setup.",
            )

            SectionHeader("Register")

            Button(
                onClick = onRegister,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Register Passkey")
            }

            uiState.registrationResult?.let {
                ResultDisplay(label = "Registration response", content = it)
            }

            SectionHeader("Authenticate")

            OutlinedButton(
                onClick = onAuthenticate,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Sign In with Passkey")
            }

            uiState.authenticationResult?.let {
                ResultDisplay(label = "Authentication response", content = it)
            }

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            Spacer(Modifier.height(8.dp))
            SectionHeader("RP ID in use")
            ResultDisplay(label = "Relying Party ID", content = MockPasskeyData.RP_ID)

            SectionHeader("Registration request JSON")
            ResultDisplay(label = "PublicKeyCredentialCreationOptions", content = MockPasskeyData.registrationRequest)

            SectionHeader("Authentication request JSON")
            ResultDisplay(label = "PublicKeyCredentialRequestOptions", content = MockPasskeyData.authenticationRequest)
        }
    }
}
