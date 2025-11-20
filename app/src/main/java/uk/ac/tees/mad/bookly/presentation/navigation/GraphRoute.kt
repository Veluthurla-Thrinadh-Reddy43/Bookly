package uk.ac.tees.mad.bookly.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface GraphRoute {
    @Serializable
    data object Splash : GraphRoute

    @Serializable
    data object Login : GraphRoute

    @Serializable
    data object ForgotPassword : GraphRoute

    @Serializable
    data object Main : GraphRoute

    @Serializable
    data class BookDetails(val bookId: String) : GraphRoute

    @Serializable
    data object Notifications : GraphRoute
}