package info.yuryv.androidx_credentials_demo.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import info.yuryv.androidx_credentials_demo.ui.components.InfoBanner
import info.yuryv.androidx_credentials_demo.ui.components.ResultDisplay
import info.yuryv.androidx_credentials_demo.ui.components.SectionHeader
import info.yuryv.androidx_credentials_demo.viewmodel.PasswordUiState
import info.yuryv.androidx_credentials_demo.viewmodel.PasswordViewModel

@Composable
fun PasswordScreen(
    viewModel: PasswordViewModel,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val activity = LocalContext.current as Activity
    PasswordScreenContent(
        uiState = uiState,
        onBack = onBack,
        onUsernameChange = viewModel::onUsernameChange,
        onPasswordChange = viewModel::onPasswordChange,
        onSave = { viewModel.savePassword(activity) },
        onRetrieve = { viewModel.getSavedPassword(activity) },
        onClearError = viewModel::clearError,
        onClearSaveSuccess = viewModel::clearSaveSuccess,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordScreenContent(
    uiState: PasswordUiState,
    onBack: () -> Unit,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSave: () -> Unit,
    onRetrieve: () -> Unit,
    onClearError: () -> Unit,
    onClearSaveSuccess: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        val msg = uiState.errorMessage ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(msg)
        onClearError()
    }

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            snackbarHostState.showSnackbar("Password saved.")
            onClearSaveSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Passwords") },
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
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SectionHeader("Save a password")

            OutlinedTextField(
                value = uiState.username,
                onValueChange = onUsernameChange,
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = uiState.password,
                onValueChange = onPasswordChange,
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
            )

            Button(
                onClick = onSave,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Save Password")
            }

            SectionHeader("Retrieve a saved password")

            InfoBanner(
                "The system will prompt you to select from credentials saved by your autofill provider. " +
                    "On emulators without a password manager configured, expect NoCredentialException.",
            )

            OutlinedButton(
                onClick = onRetrieve,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Retrieve Saved Password")
            }

            if (uiState.retrievedUsername != null) {
                ResultDisplay(label = "Retrieved username", content = uiState.retrievedUsername)
            }
            if (uiState.retrievedPassword != null) {
                // Showing the password value is intentional in this demo to illustrate
                // what the API returns. In production, never display or store plaintext passwords.
                ResultDisplay(
                    label = "Retrieved password (demo only — never display in production)",
                    content = uiState.retrievedPassword,
                )
            }

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
