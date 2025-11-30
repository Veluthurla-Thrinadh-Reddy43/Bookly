package uk.ac.tees.mad.bookly.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    // TODO: Inject your AuthRepository or UserRepository here
    // private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    init {
        // Load the user's profile data when the ViewModel is created
        loadUserProfile()
    }

    fun onAction(action: ProfileAction) {
        when (action) {
            is ProfileAction.OnToggleReminders -> {
                _state.update { it.copy(areRemindersEnabled = action.isEnabled) }
                // TODO: Save this preference to a repository
            }
            is ProfileAction.OnReminderTimeChanged -> {
                _state.update { it.copy(reminderTime = action.time) }
                // TODO: Save this preference to a repository
            }
            ProfileAction.OnLogoutClicked -> {
                logoutUser()
            }
            ProfileAction.OnDeleteAccountClicked -> {
                // TODO: Implement account deletion logic
            }
            ProfileAction.OnEditProfileClicked -> {
                // TODO: Handle navigation to an edit profile screen
            }
            ProfileAction.OnNotificationsClicked -> {
                // TODO: Handle navigation to a notifications screen
            }
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            // TODO: Replace with actual data fetching from your repository
            // val result = userRepository.getProfile()
            // For now, we use the default state values which match the image
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun logoutUser() {
        viewModelScope.launch {
            // TODO: Call your repository's logout function
            // authRepository.logout()
        }
    }
}
