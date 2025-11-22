package uk.ac.tees.mad.bookly.data

import android.util.Log
import uk.ac.tees.mad.bookly.data.local.BookDao
import uk.ac.tees.mad.bookly.domain.Book
import uk.ac.tees.mad.bookly.domain.BookRemoteDataSource
import uk.ac.tees.mad.bookly.domain.BookRepository
import uk.ac.tees.mad.bookly.domain.util.DataError
import uk.ac.tees.mad.bookly.domain.util.Result
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val remoteDataSource: BookRemoteDataSource,
    private val bookDao: BookDao
) : BookRepository {

    override suspend fun searchBooks(query: String, maxResults: Int, startIndex: Int): Result<List<Book>, DataError.Remote> {
        val remoteResult = remoteDataSource.searchBooks(query, maxResults, startIndex)

        return if (remoteResult is Result.Success) {
            // Network call succeeded, cache the results
            val books = remoteResult.data.toDomain()
            if (startIndex == 0) { // Only cache the first page
                bookDao.deleteBooksByQuery(query)
                val entities = books.map { it.toEntity(query) }
                bookDao.insertBooks(entities)
            }
            Result.Success(books)
        } else {
            // Network call failed, fall back to cache
            Log.e("BookRepository", "Network failed, falling back to cache. Error: ${(remoteResult as Result.Failure).error}")
            val cachedBooks = bookDao.getBooksByQuery(query).map { it.toBook() }
            Result.Success(cachedBooks) // Return cached books, even if the list is empty
        }
    }

    override suspend fun getBookById(bookId: String): Result<Book, DataError.Remote> {
        // Try cache first for single book details as well
        val cachedBook = bookDao.getBookById(bookId)?.toBook()
        if (cachedBook != null) {
            return Result.Success(cachedBook)
        }

        // If not in cache, try network
        val remoteResult = remoteDataSource.getBookById(bookId)

        return if (remoteResult is Result.Success) {
            val book = remoteResult.data.volumeInfo!!.toDomain(remoteResult.data.id)
            bookDao.insertBooks(listOf(book.toEntity(""))) // Cache the detailed book
            Result.Success(book)
        } else {
            // If network fails and it wasn't in cache, it's a definitive failure.
            remoteResult as Result.Failure
        }
    }
}