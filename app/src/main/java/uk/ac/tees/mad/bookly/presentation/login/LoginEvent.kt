package uk.ac.tees.mad.bookly.presentation.login

sealed interface LoginEvent {
    data object onSuccess: LoginEvent
}