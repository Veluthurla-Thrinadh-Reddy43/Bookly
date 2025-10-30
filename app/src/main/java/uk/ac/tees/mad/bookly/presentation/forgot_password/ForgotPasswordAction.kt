package uk.ac.tees.mad.bookly.presentation.forgot_password

sealed interface ForgotPasswordAction {
    data class OnEmailChanged(val email: String) : ForgotPasswordAction
    object SendPasswordResetEmail : ForgotPasswordAction
}