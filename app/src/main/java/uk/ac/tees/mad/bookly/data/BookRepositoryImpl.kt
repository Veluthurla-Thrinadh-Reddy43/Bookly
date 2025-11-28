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
            val books = remoteResult.data.toDomain()
            Log.d("BookRepository", "Online: Fetched ${books.size} books from network for query: $query")

            if (startIndex == 0 && books.isNotEmpty()) {
                bookDao.deleteBooksByQuery(query)
                val entities = books.map { it.toEntity(query) }
                bookDao.insertBooks(entities)
                Log.d("BookRepository", "Cached ${entities.size} books for query: $query")
            }
            Result.Success(books)
        } else {
            Log.w("BookRepository", "Network failed, falling back to cache for query: $query. Error: ${(remoteResult as Result.Failure).error}")
            val cachedBooks = bookDao.getBooksByQuery(query).map { it.toBook() }
            Result.Success(cachedBooks)
        }
    }

    override suspend fun getBookById(bookId: String): Result<Book, DataError.Remote> {
        val cachedBook = bookDao.getBookById(bookId)?.toBook()
        if (cachedBook != null) {
            return Result.Success(cachedBook)
        }

        val remoteResult = remoteDataSource.getBookById(bookId)

        return if (remoteResult is Result.Success) {
            val book = remoteResult.data.volumeInfo!!.toDomain(remoteResult.data.id)
            bookDao.insertBooks(listOf(book.toEntity("")))
            Result.Success(book)
        } else {
            remoteResult as Result.Failure
        }
    }
}