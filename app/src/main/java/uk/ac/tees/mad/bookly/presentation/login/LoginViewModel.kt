package uk.ac.tees.mad.bookly.presentation.login

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
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnEmailChanged -> _state.update { it.copy(email = action.email, error = null) }
            is LoginAction.OnPasswordChanged -> _state.update { it.copy(password = action.password, error = null) }
            is LoginAction.OnConfirmPasswordChanged -> _state.update {
                it.copy(
                    confirmPassword = action.confirmPassword, error = null
                )
            }
            is LoginAction.OnTabSelected -> _state.update { it.copy(selectedTab = action.selectedTab, error = null) }

            LoginAction.TogglePasswordVisibility -> _state.update {
                it.copy(
                    isPasswordVisible = !it.isPasswordVisible
                )
            }

            LoginAction.ToggleConfirmPasswordVisibility -> _state.update {
                it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible)
            }
            LoginAction.Login -> onLogin()
            LoginAction.SignUp -> onSignUp()
        }
    }

    private fun onLogin() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            authRepository.login(state.value.email, state.value.password)
                .onSuccess {
                    _state.update { it.copy(isLoading = false, isLoginSuccess = true) }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error) }
                }
        }
    }

    private fun onSignUp() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            authRepository.signup(state.value.email, state.value.password)
                .onSuccess {
                    _state.update { it.copy(
                        isLoading = false,
                        selectedTab = 0,
                        email = "",
                        password = "",
                        confirmPassword = "",
                        isPasswordVisible = false,
                        isConfirmPasswordVisible = false,
                        error = null
                    ) }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error) }
                }
        }
    }
}