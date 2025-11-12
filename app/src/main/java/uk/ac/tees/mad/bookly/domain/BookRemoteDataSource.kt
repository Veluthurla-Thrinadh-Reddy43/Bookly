package uk.ac.tees.mad.bookly.domain

import uk.ac.tees.mad.bookly.data.dtos.BookItem
import uk.ac.tees.mad.bookly.data.dtos.GoogleBooksResponse
import uk.ac.tees.mad.bookly.domain.util.DataError
import uk.ac.tees.mad.bookly.domain.util.Result

interface BookRemoteDataSource {
    suspend fun searchBooks(
        query: String,
        maxResults: Int,
        startIndex: Int
    ): Result<GoogleBooksResponse, DataError.Remote>

    suspend fun getBookById(bookId: String): Result<BookItem, DataError.Remote>
}