package info.yuryv.androidx_credentials_demo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import info.yuryv.androidx_credentials_demo.ui.components.CredentialCard
import info.yuryv.androidx_credentials_demo.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onNavigate: (Screen) -> Unit) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Credential Manager") },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { padding ->
        LazyColumn(
            contentPadding =
                PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = padding.calculateTopPadding() + 8.dp,
                    bottom = padding.calculateBottomPadding() + 16.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                CredentialCard(
                    title = "Passkeys",
                    description = "Create and authenticate with platform passkeys (FIDO2/WebAuthn)",
                    icon = Icons.Outlined.Key,
                    onClick = { onNavigate(Screen.Passkey) },
                )
            }
            item {
                CredentialCard(
                    title = "Passwords",
                    description = "Save and retrieve password credentials via autofill",
                    icon = Icons.Outlined.Lock,
                    onClick = { onNavigate(Screen.Password) },
                )
            }
            item {
                CredentialCard(
                    title = "Google Sign-In",
                    description = "Sign in with a Google account and receive an ID token",
                    icon = Icons.Outlined.AccountCircle,
                    onClick = { onNavigate(Screen.GoogleSignIn) },
                )
            }
            item {
                CredentialCard(
                    title = "About",
                    description = "How this demo works, setup requirements, and API notes",
                    icon = Icons.Outlined.Info,
                    onClick = { onNavigate(Screen.About) },
                )
            }
        }
    }
}
