package info.yuryv.androidx_credentials_demo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import info.yuryv.androidx_credentials_demo.data.CredentialRepository
import info.yuryv.androidx_credentials_demo.ui.screens.AboutScreen
import info.yuryv.androidx_credentials_demo.ui.screens.GoogleSignInScreen
import info.yuryv.androidx_credentials_demo.ui.screens.HomeScreen
import info.yuryv.androidx_credentials_demo.ui.screens.PasskeyScreen
import info.yuryv.androidx_credentials_demo.ui.screens.PasswordScreen
import info.yuryv.androidx_credentials_demo.viewmodel.GoogleSignInViewModel
import info.yuryv.androidx_credentials_demo.viewmodel.PasskeyViewModel
import info.yuryv.androidx_credentials_demo.viewmodel.PasswordViewModel

@Composable
fun AppNavigation(repository: CredentialRepository) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
    ) {
        composable(Screen.Home.route) {
            HomeScreen(onNavigate = { navController.navigate(it.route) })
        }

        composable(Screen.Passkey.route) {
            val vm: PasskeyViewModel =
                viewModel(
                    factory = PasskeyViewModel.factory(repository),
                )
            PasskeyScreen(viewModel = vm, onBack = { navController.popBackStack() })
        }

        composable(Screen.Password.route) {
            val vm: PasswordViewModel =
                viewModel(
                    factory = PasswordViewModel.factory(repository),
                )
            PasswordScreen(viewModel = vm, onBack = { navController.popBackStack() })
        }

        composable(Screen.GoogleSignIn.route) {
            val vm: GoogleSignInViewModel =
                viewModel(
                    factory = GoogleSignInViewModel.factory(repository),
                )
            GoogleSignInScreen(viewModel = vm, onBack = { navController.popBackStack() })
        }

        composable(Screen.About.route) {
            AboutScreen(onBack = { navController.popBackStack() })
        }
    }
}
