package info.yuryv.androidx_credentials_demo.ui.navigation

sealed class Screen(
    val route: String,
) {
    data object Home : Screen("home")

    data object Passkey : Screen("passkey")

    data object Password : Screen("password")

    data object GoogleSignIn : Screen("google_sign_in")

    data object About : Screen("about")
}
