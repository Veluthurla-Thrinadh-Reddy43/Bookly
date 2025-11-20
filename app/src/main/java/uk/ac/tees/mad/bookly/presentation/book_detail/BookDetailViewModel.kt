package uk.ac.tees.mad.bookly.presentation.book_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.bookly.domain.Book
import uk.ac.tees.mad.bookly.domain.BookRepository
import uk.ac.tees.mad.bookly.domain.ReadingListRepository
import uk.ac.tees.mad.bookly.domain.util.DataError
import uk.ac.tees.mad.bookly.domain.util.Result
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val readingListRepository: ReadingListRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(BookDetailState())
    val state = _state.asStateFlow()

    init {
        val bookId = savedStateHandle.get<String>("bookId")
        if (bookId != null) {
            loadBookDetails(bookId)
            observeReadingList(bookId)
        } else {
            _state.update { it.copy(error = DataError.Remote.NOT_FOUND) }
        }
    }

    fun onAction(action: BookDetailAction) {
        when (action) {
            is BookDetailAction.OnSimilarBookClicked -> {
                loadBookDetails(action.bookId)
            }
            BookDetailAction.OnAddToListClicked -> {
                viewModelScope.launch {
                    val book = state.value.book ?: return@launch
                    if (state.value.isInReadingList) {
                        readingListRepository.removeBookFromList(book.id)
                    } else {
                        readingListRepository.addBookToList(book)
                    }
                }
            }
            else -> {}
        }
    }

    private fun observeReadingList(bookId: String) {
        readingListRepository.getReadingList()
            .distinctUntilChanged()
            .onEach { list ->
                _state.update { it.copy(isInReadingList = list.any { book -> book.id == bookId }) }
            }
            .launchIn(viewModelScope)
    }

    private fun loadBookDetails(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, book = null, similarBooks = emptyList(), error = null) }

            val bookResult = bookRepository.getBookById(id)
            if (bookResult is Result.Failure) {
                _state.update { it.copy(isLoading = false, error = bookResult.error) }
                return@launch
            }
            val book = (bookResult as Result.Success).data

            val author = book.authors.firstOrNull()
            val similarBooks = if (author != null && author.isNotBlank()) {
                when (val similarResult = bookRepository.searchBooks(author, 20, 0)) {
                    is Result.Success -> similarResult.data.filter { it.id != book.id }
                    is Result.Failure -> emptyList() // Non-critical, so we can fail silently
                }
            } else {
                emptyList()
            }

            // Update state with all data at once
            _state.update {
                it.copy(
                    isLoading = false,
                    book = book,
                    similarBooks = similarBooks
                )
            }
        }
    }
}