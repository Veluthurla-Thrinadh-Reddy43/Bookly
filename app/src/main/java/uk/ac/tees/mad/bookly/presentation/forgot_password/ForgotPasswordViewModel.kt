package uk.ac.tees.mad.bookly.presentation.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.bookly.domain.AuthRepository
import uk.ac.tees.mad.bookly.domain.util.onFailure
import uk.ac.tees.mad.bookly.domain.util.onSuccess

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ForgotPasswordState())
    val state = _state.asStateFlow()

    fun onAction(action: ForgotPasswordAction) {
        when (action) {
            is ForgotPasswordAction.OnEmailChanged -> _state.update { it.copy(email = action.email, error = null, successMessage = null) }
            ForgotPasswordAction.SendPasswordResetEmail -> onSendPasswordResetEmail()
        }
    }

    private fun onSendPasswordResetEmail() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, successMessage = null) }
            authRepository.sendPasswordResetEmail(state.value.email)
                .onSuccess {
                    _state.update { it.copy(isLoading = false, successMessage = "Password reset email sent successfully.") }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error) }
                }
        }
    }
}