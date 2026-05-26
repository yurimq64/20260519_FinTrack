package br.unifor.fintrack

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import br.unifor.fintrack.ui.navigation.FinTrackNavHost
import br.unifor.fintrack.ui.theme.FinTrackTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        FirebaseApp.initializeApp(this)
        Log.i("FinTrack", "Firebase apps: ${ FirebaseApp.getApps(this).size }")
        Log.i("FinTrack", "Firebase Auth: ${FirebaseAuth.getInstance().app.name}")

        setContent {
            FinTrackTheme {
                FinTrackNavHost()
            }
        }
    }
}