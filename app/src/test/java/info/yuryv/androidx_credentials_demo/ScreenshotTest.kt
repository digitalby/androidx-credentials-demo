package info.yuryv.androidx_credentials_demo

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.captureRoboImage
import info.yuryv.androidx_credentials_demo.ui.screens.AboutScreen
import info.yuryv.androidx_credentials_demo.ui.screens.GoogleSignInScreenContent
import info.yuryv.androidx_credentials_demo.ui.screens.HomeScreen
import info.yuryv.androidx_credentials_demo.ui.screens.PasskeyScreenContent
import info.yuryv.androidx_credentials_demo.ui.screens.PasswordScreenContent
import info.yuryv.androidx_credentials_demo.ui.theme.AndroidxcredentialsdemoTheme
import info.yuryv.androidx_credentials_demo.viewmodel.GoogleSignInUiState
import info.yuryv.androidx_credentials_demo.viewmodel.PasskeyUiState
import info.yuryv.androidx_credentials_demo.viewmodel.PasswordUiState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Screenshot tests using Roborazzi + Robolectric (no emulator required).
 *
 * **Generate/update screenshots:**
 * ```
 * ./gradlew testDebugUnitTest --tests "*.ScreenshotTest" -Proborazzi.test.record=true
 * ```
 * Commit the resulting PNGs under app/src/test/snapshots/ to the repo.
 *
 * **Verify (default — CI fails if screenshots changed without regenerating):**
 * ```
 * ./gradlew testDebugUnitTest --tests "*.ScreenshotTest"
 * ```
 *
 * ViewModel-dependent screens are tested via their state-hoisted *Content composables,
 * which accept a plain UiState data class and callback lambdas — no ViewModel required.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34])
class ScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ── HomeScreen ────────────────────────────────────────────────────────────

    @Test
    fun homeScreen_light() {
        composeTestRule.setContent {
            AndroidxcredentialsdemoTheme(darkTheme = false, dynamicColor = false) {
                HomeScreen(onNavigate = {})
            }
        }
        composeTestRule.onRoot().captureRoboImage(
            filePath = "src/test/snapshots/homeScreen_light.png",
        )
    }

    @Test
    fun homeScreen_dark() {
        composeTestRule.setContent {
            AndroidxcredentialsdemoTheme(darkTheme = true, dynamicColor = false) {
                HomeScreen(onNavigate = {})
            }
        }
        composeTestRule.onRoot().captureRoboImage(
            filePath = "src/test/snapshots/homeScreen_dark.png",
        )
    }

    // ── AboutScreen ───────────────────────────────────────────────────────────

    @Test
    fun aboutScreen_light() {
        composeTestRule.setContent {
            AndroidxcredentialsdemoTheme(darkTheme = false, dynamicColor = false) {
                AboutScreen(onBack = {})
            }
        }
        composeTestRule.onRoot().captureRoboImage(
            filePath = "src/test/snapshots/aboutScreen_light.png",
        )
    }

    // ── PasskeyScreen ─────────────────────────────────────────────────────────

    @Test
    fun passkeyScreen_default_light() {
        composeTestRule.setContent {
            AndroidxcredentialsdemoTheme(darkTheme = false, dynamicColor = false) {
                PasskeyScreenContent(
                    uiState = PasskeyUiState(),
                    onBack = {},
                    onRegister = {},
                    onAuthenticate = {},
                    onClearError = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage(
            filePath = "src/test/snapshots/passkeyScreen_default_light.png",
        )
    }

    @Test
    fun passkeyScreen_withResult_light() {
        composeTestRule.setContent {
            AndroidxcredentialsdemoTheme(darkTheme = false, dynamicColor = false) {
                PasskeyScreenContent(
                    uiState = PasskeyUiState(
                        registrationResult = """{"id":"abc123","type":"public-key","response":{}}""",
                    ),
                    onBack = {},
                    onRegister = {},
                    onAuthenticate = {},
                    onClearError = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage(
            filePath = "src/test/snapshots/passkeyScreen_withResult_light.png",
        )
    }

    // ── PasswordScreen ────────────────────────────────────────────────────────

    @Test
    fun passwordScreen_default_light() {
        composeTestRule.setContent {
            AndroidxcredentialsdemoTheme(darkTheme = false, dynamicColor = false) {
                PasswordScreenContent(
                    uiState = PasswordUiState(),
                    onBack = {},
                    onUsernameChange = {},
                    onPasswordChange = {},
                    onSave = {},
                    onRetrieve = {},
                    onClearError = {},
                    onClearSaveSuccess = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage(
            filePath = "src/test/snapshots/passwordScreen_default_light.png",
        )
    }

    @Test
    fun passwordScreen_withRetrievedCredentials_light() {
        composeTestRule.setContent {
            AndroidxcredentialsdemoTheme(darkTheme = false, dynamicColor = false) {
                PasswordScreenContent(
                    uiState = PasswordUiState(
                        username = "alice@example.com",
                        password = "hunter2",
                        retrievedUsername = "alice@example.com",
                        retrievedPassword = "hunter2",
                    ),
                    onBack = {},
                    onUsernameChange = {},
                    onPasswordChange = {},
                    onSave = {},
                    onRetrieve = {},
                    onClearError = {},
                    onClearSaveSuccess = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage(
            filePath = "src/test/snapshots/passwordScreen_withRetrievedCredentials_light.png",
        )
    }

    // ── GoogleSignInScreen ────────────────────────────────────────────────────

    @Test
    fun googleSignInScreen_placeholder_light() {
        composeTestRule.setContent {
            AndroidxcredentialsdemoTheme(darkTheme = false, dynamicColor = false) {
                GoogleSignInScreenContent(
                    uiState = GoogleSignInUiState(isClientIdMissing = true),
                    onBack = {},
                    onSignIn = {},
                    onSignOut = {},
                    onClearError = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage(
            filePath = "src/test/snapshots/googleSignInScreen_placeholder_light.png",
        )
    }

    @Test
    fun googleSignInScreen_signedIn_light() {
        composeTestRule.setContent {
            AndroidxcredentialsdemoTheme(darkTheme = false, dynamicColor = false) {
                GoogleSignInScreenContent(
                    uiState = GoogleSignInUiState(
                        isSignedIn = true,
                        userDisplayName = "Alice Example",
                        userEmail = "alice@example.com",
                    ),
                    onBack = {},
                    onSignIn = {},
                    onSignOut = {},
                    onClearError = {},
                )
            }
        }
        composeTestRule.onRoot().captureRoboImage(
            filePath = "src/test/snapshots/googleSignInScreen_signedIn_light.png",
        )
    }
}
