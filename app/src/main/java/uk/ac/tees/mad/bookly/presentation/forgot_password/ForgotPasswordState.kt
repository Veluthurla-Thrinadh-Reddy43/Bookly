package uk.ac.tees.mad.bookly.presentation.forgot_password

import uk.ac.tees.mad.bookly.domain.util.DataError

data class ForgotPasswordState(
    val email: String = "",
    val isLoading: Boolean = false,
    val error: DataError.Firebase? = null,
    val successMessage: String? = null
)