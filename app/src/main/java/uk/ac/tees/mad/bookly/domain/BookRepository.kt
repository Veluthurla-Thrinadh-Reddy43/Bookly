package uk.ac.tees.mad.bookly.domain

import uk.ac.tees.mad.bookly.domain.util.DataError
import uk.ac.tees.mad.bookly.domain.util.Result

interface BookRepository {
    suspend fun searchBooks(
        query: String,
        maxResults: Int,
        startIndex: Int
    ): Result<List<Book>, DataError.Remote>

    suspend fun getBookById(bookId: String): Result<Book, DataError.Remote>
}