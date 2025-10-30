package uk.ac.tees.mad.bookly.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import uk.ac.tees.mad.bookly.presentation.MainScreen
import uk.ac.tees.mad.bookly.presentation.forgot_password.ForgotPasswordRoot
import uk.ac.tees.mad.bookly.presentation.login.LoginRoot

@Composable
fun NavigationRoot(navController: NavHostController) {

    val startDestination = if (FirebaseAuth.getInstance().currentUser != null) {
        GraphRoute.Main
    } else {
        GraphRoute.Login
    }

    NavHost(navController = navController, startDestination = startDestination) {

        composable<GraphRoute.Login> {
            LoginRoot(
                onLoginSuccess = {
                    navController.navigate(GraphRoute.Main) {
                        popUpTo(GraphRoute.Login) {
                            inclusive = true
                        }
                    }
                },
                onForgotPasswordClicked = {
                    navController.navigate(GraphRoute.ForgotPassword)
                }
            )
        }

        composable<GraphRoute.ForgotPassword> {
            ForgotPasswordRoot()
        }

        composable<GraphRoute.Main> {
            MainScreen()
        }
    }
}