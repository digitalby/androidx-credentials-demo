package info.yuryv.androidx_credentials_demo

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented navigation tests.
 *
 * These run on a device or emulator. They verify that tapping each home card navigates
 * to the correct screen and that the back button returns to the home screen.
 *
 * Note: These tests do NOT exercise actual Credential Manager API calls — the credential
 * flows require a real autofill provider and system UI interaction that cannot be driven
 * by Compose test rules. See the README for what is testable programmatically.
 */
@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun homeScreen_showsAllCards() {
        composeTestRule.onNodeWithText("Passkeys").assertIsDisplayed()
        composeTestRule.onNodeWithText("Passwords").assertIsDisplayed()
        composeTestRule.onNodeWithText("Google Sign-In").assertIsDisplayed()
        composeTestRule.onNodeWithText("About").assertIsDisplayed()
    }

    @Test
    fun tappingPasskeys_navigatesToPasskeyScreen() {
        composeTestRule.onNodeWithText("Passkeys").performClick()
        composeTestRule.onNodeWithText("Register Passkey").assertIsDisplayed()
    }

    @Test
    fun tappingPasskeys_backButton_returnsHome() {
        composeTestRule.onNodeWithText("Passkeys").performClick()
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        composeTestRule.onNodeWithText("Passkeys").assertIsDisplayed()
    }

    @Test
    fun tappingPasswords_navigatesToPasswordScreen() {
        composeTestRule.onNodeWithText("Passwords").performClick()
        composeTestRule.onNodeWithText("Save Password").assertIsDisplayed()
    }

    @Test
    fun tappingGoogleSignIn_navigatesToGoogleSignInScreen() {
        composeTestRule.onNodeWithText("Google Sign-In").performClick()
        composeTestRule.onNodeWithText("Sign In with Google").assertIsDisplayed()
    }

    @Test
    fun tappingAbout_navigatesToAboutScreen() {
        composeTestRule.onNodeWithText("About").performClick()
        composeTestRule.onNodeWithText("What is Credential Manager?").assertIsDisplayed()
    }
}

// Extension to search by content description — cleaner than the raw string overload
private fun androidx.compose.ui.test.SemanticsNodeInteractionsProvider.onNodeWithContentDescription(
    description: String,
) = onNode(
    androidx.compose.ui.test.hasContentDescription(description),
)
