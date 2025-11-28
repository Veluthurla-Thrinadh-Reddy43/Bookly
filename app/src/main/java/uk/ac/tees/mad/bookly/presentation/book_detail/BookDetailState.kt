package uk.ac.tees.mad.bookly.presentation.book_detail

import uk.ac.tees.mad.bookly.domain.Book
import uk.ac.tees.mad.bookly.domain.util.DataError

data class BookDetailState(
    val book: Book? = null,
    val similarBooks: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val error: DataError? = null,
    val isOffline: Boolean = false,
    val isInReadingList: Boolean = false // Renamed for clarity
)
