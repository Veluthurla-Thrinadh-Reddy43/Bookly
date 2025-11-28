package uk.ac.tees.mad.bookly.presentation.reading_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import uk.ac.tees.mad.bookly.domain.ReadingListRepository
import javax.inject.Inject

@HiltViewModel
class ReadingListViewModel @Inject constructor(
    private val readingListRepository: ReadingListRepository
) : ViewModel() {

    val state = readingListRepository.getReadingList()
        .map {
            ReadingListState(
                books = it,
                totalBooks = it.size,
                finishedBooks = it.count { book -> book.status == ReadingStatus.Finished },
                inProgressBooks = it.count { book -> book.status == ReadingStatus.InProgress }
            )
        }
        .onStart { emit(ReadingListState(isLoading = true)) }
        .catch { emit(ReadingListState(error = "Error loading reading list")) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ReadingListState(isLoading = true))

    init {
        // Sync with Firebase on startup
        viewModelScope.launch {
            readingListRepository.syncReadingList()
        }
    }

    fun onAction(action: ReadingListAction) {
        when (action) {
            is ReadingListAction.OnDeleteClicked -> {
                viewModelScope.launch {
                    readingListRepository.removeBookFromList(action.bookId)
                }
            }
            is ReadingListAction.OnEditClicked -> {
                viewModelScope.launch {
                    // Find the book in the current state to get its status
                    val book = state.value.books.firstOrNull { it.id == action.bookId }
                    if (book != null) {
                        val newStatus = if (book.status == ReadingStatus.InProgress) ReadingStatus.Finished else ReadingStatus.InProgress
                        readingListRepository.updateBookStatus(action.bookId, newStatus)
                    }
                }
            }
            else -> {}
        }
    }
}