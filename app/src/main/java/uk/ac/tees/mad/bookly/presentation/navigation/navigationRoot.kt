package uk.ac.tees.mad.bookly.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import uk.ac.tees.mad.bookly.presentation.MainScreen
import uk.ac.tees.mad.bookly.presentation.book_detail.BookDetailRoot
import uk.ac.tees.mad.bookly.presentation.forgot_password.ForgotPasswordRoot
import uk.ac.tees.mad.bookly.presentation.login.LoginRoot
import uk.ac.tees.mad.bookly.presentation.splash.SplashScreen

@Composable
fun NavigationRoot(navController: NavHostController) {

    NavHost(navController = navController, startDestination = GraphRoute.Splash) {

        composable<GraphRoute.Splash> {
            SplashScreen(onSplashFinished = {
                val destination = if (FirebaseAuth.getInstance().currentUser != null) {
                    GraphRoute.Main
                } else {
                    GraphRoute.Login
                }
                navController.navigate(destination) {
                    popUpTo(GraphRoute.Splash) { inclusive = true }
                }
            })
        }

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
            MainScreen(navController)
        }

        composable<GraphRoute.BookDetails> { backStackEntry ->
            BookDetailRoot(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}