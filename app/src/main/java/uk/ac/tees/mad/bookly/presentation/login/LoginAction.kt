package uk.ac.tees.mad.bookly.presentation.login

sealed interface LoginAction {
    data class OnEmailChanged(val email: String) : LoginAction
    data class OnPasswordChanged(val password: String) : LoginAction
    data class OnConfirmPasswordChanged(val confirmPassword: String) : LoginAction
    data class OnTabSelected(val selectedTab: Int) : LoginAction
    object TogglePasswordVisibility : LoginAction
    object ToggleConfirmPasswordVisibility : LoginAction
    object Login : LoginAction
    object SignUp : LoginAction
}