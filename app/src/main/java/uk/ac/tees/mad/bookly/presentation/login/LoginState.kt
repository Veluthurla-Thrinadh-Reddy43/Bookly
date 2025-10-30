package uk.ac.tees.mad.bookly.presentation.login

import uk.ac.tees.mad.bookly.domain.util.DataError

data class LoginState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val selectedTab: Int = 0,
    val error: DataError.Firebase? = null,
    val isLoginSuccess: Boolean = false
)