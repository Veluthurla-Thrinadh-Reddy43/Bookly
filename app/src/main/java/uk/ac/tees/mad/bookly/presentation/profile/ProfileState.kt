package uk.ac.tees.mad.bookly.presentation.profile

import java.time.LocalTime

// Represents the entire state of the Profile screen
data class ProfileState(
    val userName: String = "Aria Sharma",
    val userEmail: String = "aria.sharma@bookly.com",
    val profileImageUrl: String = "", // URL for the profile picture
    val booksSaved: Int = 124,
    val booksCompleted: Int = 87,
    val totalReadingTimeHours: Int = 340,
    val areRemindersEnabled: Boolean = true,
    val reminderTime: LocalTime = LocalTime.of(19, 0),
    val isLoading: Boolean = false,
    val error: String? = null
)
