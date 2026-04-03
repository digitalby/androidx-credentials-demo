package info.yuryv.androidx_credentials_demo

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.captureRoboImage
import info.yuryv.androidx_credentials_demo.ui.screens.AboutScreen
import info.yuryv.androidx_credentials_demo.ui.screens.HomeScreen
import info.yuryv.androidx_credentials_demo.ui.theme.AndroidxcredentialsdemoTheme
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
 * Only HomeScreen and AboutScreen are covered because they take no ViewModel parameters.
 * Screens with ViewModels need state-hoisted variants before they can be snapshot-tested.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34])
class ScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

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
}
