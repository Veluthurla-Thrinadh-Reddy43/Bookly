package uk.ac.tees.mad.bookly.presentation.home

import uk.ac.tees.mad.bookly.domain.Book
import uk.ac.tees.mad.bookly.domain.util.DataError

// Represents the entire state of the home screen
data class HomeState(
    val searchQuery: String = "",
    val books: List<Book> = emptyList(),
    val recentSearches: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: DataError.Remote? = null,
    val infoMessage: String? = null
)