package uk.ac.tees.mad.bookly.domain

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.bookly.domain.util.DataError
import uk.ac.tees.mad.bookly.domain.util.EmptyResult
import uk.ac.tees.mad.bookly.presentation.reading_list.ReadingListBook
import uk.ac.tees.mad.bookly.presentation.reading_list.ReadingStatus

interface ReadingListRepository {
    fun getReadingList(): Flow<List<ReadingListBook>>
    suspend fun addBookToList(book: Book): EmptyResult<DataError.Firebase>
    suspend fun removeBookFromList(bookId: String): EmptyResult<DataError.Firebase>
    suspend fun updateBookStatus(bookId: String, status: ReadingStatus): EmptyResult<DataError.Firebase>
    suspend fun syncReadingList(): EmptyResult<DataError.Firebase>
}