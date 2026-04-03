package info.yuryv.androidx_credentials_demo.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import info.yuryv.androidx_credentials_demo.ui.components.InfoBanner
import info.yuryv.androidx_credentials_demo.ui.components.SectionHeader
import info.yuryv.androidx_credentials_demo.viewmodel.GoogleSignInViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoogleSignInScreen(
    viewModel: GoogleSignInViewModel,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val activity = LocalContext.current as Activity

    LaunchedEffect(uiState.errorMessage) {
        val msg = uiState.errorMessage ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(msg)
        viewModel.clearError()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Google Sign-In") },
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
            if (uiState.isClientIdMissing) {
                InfoBanner(
                    "No Google Web client ID configured. To use Google Sign-In:\n" +
                        "1. Create a project in the Google Cloud Console.\n" +
                        "2. Add an OAuth 2.0 Web client ID.\n" +
                        "3. Replace the placeholder in app/build.gradle.kts under " +
                        "buildConfigField(\"GOOGLE_WEB_CLIENT_ID\").",
                )
            }

            if (uiState.isSignedIn) {
                SectionHeader("Signed in")
                SignedInCard(
                    displayName = uiState.userDisplayName,
                    email = uiState.userEmail,
                )
            }

            Button(
                onClick = { viewModel.signInWithGoogle(activity) },
                enabled = !uiState.isLoading && !uiState.isSignedIn,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Sign In with Google")
            }

            OutlinedButton(
                onClick = { viewModel.signOut() },
                enabled = !uiState.isLoading && uiState.isSignedIn,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Sign Out")
            }

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun SignedInCard(displayName: String?, email: String?) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
    ) {
        // Avatar placeholder — first letter of display name in a circle
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(72.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = displayName?.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        if (displayName != null) {
            Text(displayName, style = MaterialTheme.typography.titleMedium)
        }
        if (email != null) {
            Text(
                text = email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}
