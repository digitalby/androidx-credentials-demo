package info.yuryv.androidx_credentials_demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import info.yuryv.androidx_credentials_demo.data.CredentialRepository
import info.yuryv.androidx_credentials_demo.ui.navigation.AppNavigation
import info.yuryv.androidx_credentials_demo.ui.theme.AndroidxcredentialsdemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repository = CredentialRepository(applicationContext)
        setContent {
            AndroidxcredentialsdemoTheme {
                AppNavigation(repository = repository)
            }
        }
    }
}
