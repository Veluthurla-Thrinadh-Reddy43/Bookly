package uk.ac.tees.mad.bookly.presentation.profile

import java.time.LocalTime

sealed interface ProfileAction {
    // When the user clicks the notification icon
    object OnNotificationsClicked : ProfileAction
    // When the user clicks the "Edit Profile" button
    object OnEditProfileClicked : ProfileAction
    // When the user toggles the daily reminders switch
    data class OnToggleReminders(val isEnabled: Boolean) : ProfileAction
    // When the user sets a new reminder time
    data class OnReminderTimeChanged(val time: LocalTime) : ProfileAction
    // When the user clicks the "Logout" button
    object OnLogoutClicked : ProfileAction
    // When the user clicks the "Delete Account" button
    object OnDeleteAccountClicked : ProfileAction
}
