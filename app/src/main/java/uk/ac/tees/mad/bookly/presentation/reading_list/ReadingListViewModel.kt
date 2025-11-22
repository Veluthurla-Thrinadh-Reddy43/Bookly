package uk.ac.tees.mad.bookly.presentation.reading_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadingListViewModel @Inject constructor(
    // TODO: Inject your repository e.g., private val userListRepository: UserListRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ReadingListState())
    val state = _state.asStateFlow()

    init {
        loadReadingList()
    }

    fun onAction(action: ReadingListAction) {
        when (action) {
            is ReadingListAction.OnBookClicked -> {
                // TODO: Handle navigation to book details
            }
            is ReadingListAction.OnDeleteClicked -> {
                // TODO: Implement logic to delete the book from the user's list
            }
            is ReadingListAction.OnEditClicked -> {
                // TODO: Implement logic to show an edit dialog or screen
            }
            ReadingListAction.OnNotificationsClicked -> {
                // TODO: Handle navigation to notifications screen
            }
        }
    }

    private fun loadReadingList() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            // TODO: Replace with actual data fetching from your repository
            // val result = userListRepository.getReadingList()
            // result.onSuccess { ... }.onFailure { ... }

            // Simulate a result with sample data:
            val sampleBooks = getSampleReadingList()
            _state.update {
                it.copy(
                    isLoading = false,
                    books = sampleBooks,
                    totalBooks = sampleBooks.size,
                    finishedBooks = sampleBooks.count { book -> book.status == ReadingStatus.Finished },
                    inProgressBooks = sampleBooks.count { book -> book.status == ReadingStatus.InProgress }
                )
            }
        }
    }

    // A sample data function for preview and placeholder purposes
    private fun getSampleReadingList(): List<ReadingListBook> {
        return listOf(
            ReadingListBook("1", "The Alchemist: A Fable About Following Your", "Paulo Coelho", "", ReadingStatus.InProgress),
            ReadingListBook("2", "Pride and Prejudice", "Jane Austen", "", ReadingStatus.Finished),
            ReadingListBook("3", "To Kill a Mockingbird", "Harper Lee", "", ReadingStatus.InProgress),
            ReadingListBook("4", "1984", "George Orwell", "", ReadingStatus.InProgress)
        )
    }
}
