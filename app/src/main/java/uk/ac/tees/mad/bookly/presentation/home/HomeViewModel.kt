package uk.ac.tees.mad.bookly.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.ac.tees.mad.bookly.domain.Book
import uk.ac.tees.mad.bookly.domain.BookSearchRepository
import uk.ac.tees.mad.bookly.domain.util.DataError
import uk.ac.tees.mad.bookly.domain.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bookSearchRepository: BookSearchRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        loadInitialData()
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnSearchQueryChanged -> {
                _state.update { it.copy(searchQuery = action.query, error = null) }
            }
            HomeAction.OnSearch -> {
                searchBooks(query = _state.value.searchQuery)
            }
            is HomeAction.OnBookClicked -> { /* Handled in UI */ }
            HomeAction.OnNotificationsClicked -> { /* Handled in UI */ }
        }
    }

    private fun searchBooks(query: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, infoMessage = null, error = null, books = emptyList()) }
            when (val result = performSearch(query)) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            books = result.data,
                            infoMessage = if (result.data.isEmpty()) "No results found for '$query'" else null
                        )
                    }
                }
                is Result.Failure -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.error,
                            infoMessage = "Could not load search results."
                        )
                    }
                }
            }
        }
    }

    private suspend fun performSearch(query: String): Result<List<Book>, DataError.Remote> {
        if (query.isBlank()) {
            return Result.Success(emptyList())
        }
        return bookSearchRepository.search(query)
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val lastQuery = bookSearchRepository.getLastSearchQuery()
            
            if (lastQuery.isNotEmpty()) {
                _state.update { it.copy(searchQuery = lastQuery) }
                // This search will now correctly return cached results when offline
                when (val searchResult = performSearch(lastQuery)) {
                    is Result.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                books = searchResult.data,
                                infoMessage = if (searchResult.data.isNotEmpty()) "Results from your last search are displayed." else null
                            )
                        }
                    }
                    is Result.Failure -> {
                        // This case should ideally not be hit in an offline-first setup for the initial load
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = searchResult.error,
                                infoMessage = "Could not load results from your last search."
                            )
                        }
                    }
                }
            } else {
                // No last search, just show the initial message
                _state.update { it.copy(isLoading = false, infoMessage = "Search for books to get started.") }
            }
        }
    }
}