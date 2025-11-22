package uk.ac.tees.mad.bookly.presentation.book_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.bookly.domain.BookRepository
import uk.ac.tees.mad.bookly.domain.util.DataError
import uk.ac.tees.mad.bookly.domain.util.Result
import uk.ac.tees.mad.bookly.domain.util.onSuccess
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(BookDetailState())
    val state = _state.asStateFlow()

    private val bookId: String? = savedStateHandle.get<String>("bookId")

    init {
        bookId?.let {
            loadBookDetails(it)
        } ?: _state.update { it.copy(error = DataError.Remote.NOT_FOUND) } // Corrected error type
    }

    fun onAction(action: BookDetailAction) {
        when (action) {
            is BookDetailAction.OnSimilarBookClicked -> {
                loadBookDetails(action.bookId)
            }
            else -> {}
        }
    }

    private fun loadBookDetails(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = bookRepository.getBookById(id)) {
                is Result.Success -> {
                    val book = result.data
                    _state.update { it.copy(book = book, isLoading = false) }

                    book.authors.firstOrNull()?.let { author ->
                        if (author.isNotBlank()) {
                            bookRepository.searchBooks(author, 20, 0)
                                .onSuccess { similarBooks ->
                                    _state.update { it.copy(similarBooks = similarBooks.filter { it.id != book.id }) }
                                }
                        }
                    }
                }
                is Result.Failure -> {
                    _state.update { it.copy(error = result.error, isLoading = false) }
                }
            }
        }
    }
}