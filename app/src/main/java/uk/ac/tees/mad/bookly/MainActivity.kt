package uk.ac.tees.mad.bookly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import uk.ac.tees.mad.bookly.presentation.navigation.NavigationRoot
import uk.ac.tees.mad.bookly.ui.theme.BooklyTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            BooklyTheme {
                val navController = rememberNavController()
                NavigationRoot(navController = navController)
            }
        }
    }
}