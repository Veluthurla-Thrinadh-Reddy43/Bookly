package uk.ac.tees.mad.bookly.data

import uk.ac.tees.mad.bookly.data.local.BookDao
import uk.ac.tees.mad.bookly.domain.Book
import uk.ac.tees.mad.bookly.domain.BookRemoteDataSource
import uk.ac.tees.mad.bookly.domain.BookRepository
import uk.ac.tees.mad.bookly.domain.util.DataError
import uk.ac.tees.mad.bookly.domain.util.Result
import uk.ac.tees.mad.bookly.domain.util.map
import uk.ac.tees.mad.bookly.domain.util.onSuccess
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val remoteDataSource: BookRemoteDataSource,
    private val bookDao: BookDao
) : BookRepository {

    override suspend fun searchBooks(
        query: String,
        maxResults: Int,
        startIndex: Int
    ): Result<List<Book>, DataError.Remote> {
        // For simplicity, we are not caching paginated results. 
        // A more complex implementation could store page numbers.
        if (startIndex == 0) {
            val cachedBooks = bookDao.getBooksByQuery(query).map { it.toBook() }
            if (cachedBooks.isNotEmpty()) {
                return Result.Success(cachedBooks)
            }
        }

        val result = remoteDataSource.searchBooks(query, maxResults, startIndex)

        result.onSuccess { response ->
            if (startIndex == 0) { // Only cache the first page
                bookDao.deleteBooksByQuery(query) // Clear old results for this query
                val entities = response.toDomain().map { it.toEntity(query) }
                bookDao.insertBooks(entities)
            }
        }

        return result.map { it.toDomain() }
    }
}