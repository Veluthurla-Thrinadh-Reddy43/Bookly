package uk.ac.tees.mad.bookly.data

import uk.ac.tees.mad.bookly.domain.Book
import uk.ac.tees.mad.bookly.domain.BookRepository
import uk.ac.tees.mad.bookly.domain.BookSearchRepository
import uk.ac.tees.mad.bookly.domain.SearchHistoryRepository
import uk.ac.tees.mad.bookly.domain.util.DataError
import uk.ac.tees.mad.bookly.domain.util.Result
import javax.inject.Inject

class BookSearchRepositoryImpl @Inject constructor(
    private val bookRepository: BookRepository,
    private val searchHistoryRepository: SearchHistoryRepository
) : BookSearchRepository {

    override suspend fun search(
        query: String,
        maxResults: Int,
        startIndex: Int
    ): Result<List<Book>, DataError.Remote> {
        // Don't add empty queries to search history
        if (query.isNotBlank()) {
            searchHistoryRepository.addSearchTerm(query)
        }
        return bookRepository.searchBooks(query, maxResults, startIndex)
    }

    override suspend fun recentQueries(limit: Int): Result<List<String>, DataError.Remote> {
        return when (val result = searchHistoryRepository.getRecentQueries(limit)) {
            is Result.Success -> Result.Success(result.data.distinct())
            is Result.Failure -> Result.Failure(DataError.Remote.UNKNOWN) // Or map specific errors
        }
    }
}