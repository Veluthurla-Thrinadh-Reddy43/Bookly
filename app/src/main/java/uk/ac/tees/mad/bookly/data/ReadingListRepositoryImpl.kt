package uk.ac.tees.mad.bookly.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.bookly.data.local.ReadingListDao
import uk.ac.tees.mad.bookly.data.local.toReadingListBook
import uk.ac.tees.mad.bookly.data.local.toReadingListBookEntity
import uk.ac.tees.mad.bookly.domain.Book
import uk.ac.tees.mad.bookly.domain.ReadingListRepository
import uk.ac.tees.mad.bookly.domain.util.DataError
import uk.ac.tees.mad.bookly.domain.util.EmptyResult
import uk.ac.tees.mad.bookly.domain.util.Result
import uk.ac.tees.mad.bookly.presentation.reading_list.ReadingListBook
import uk.ac.tees.mad.bookly.presentation.reading_list.ReadingStatus
import javax.inject.Inject

class ReadingListRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
    private val readingListDao: ReadingListDao
) : ReadingListRepository {

    private val userId: String? get() = firebaseAuth.currentUser?.uid

    override fun getReadingList(): Flow<List<ReadingListBook>> {
        return readingListDao.getReadingList().map { entities ->
            entities.map { it.toReadingListBook() }
        }
    }

    override suspend fun syncReadingList(): EmptyResult<DataError.Firebase> {
        if (userId == null) return Result.Failure(DataError.Firebase.UNAUTHENTICATED)

        return try {
            val snapshot = firebaseDatabase.getReference("reading_list").child(userId!!).get().await()
            val remoteList = snapshot.children.mapNotNull { it.getValue(ReadingListBook::class.java) }
            readingListDao.upsertBooks(remoteList.map { it.toReadingListBookEntity() })
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(DataError.Firebase.UNKNOWN)
        }
    }

    override suspend fun addBookToList(book: Book): EmptyResult<DataError.Firebase> {
        if (userId == null) return Result.Failure(DataError.Firebase.UNAUTHENTICATED)

        val readingListBook = ReadingListBook(
            id = book.id,
            title = book.title,
            author = book.authors.joinToString(", "),
            imageUrl = book.thumbnail ?: "",
            status = ReadingStatus.InProgress
        )

        return try {
            firebaseDatabase.getReference("reading_list").child(userId!!).child(book.id).setValue(readingListBook).await()
            readingListDao.upsertBook(readingListBook.toReadingListBookEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(DataError.Firebase.UNKNOWN)
        }
    }

    override suspend fun removeBookFromList(bookId: String): EmptyResult<DataError.Firebase> {
        if (userId == null) return Result.Failure(DataError.Firebase.UNAUTHENTICATED)

        return try {
            firebaseDatabase.getReference("reading_list").child(userId!!).child(bookId).removeValue().await()
            readingListDao.deleteBook(bookId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(DataError.Firebase.UNKNOWN)
        }
    }

    override suspend fun updateBookStatus(bookId: String, status: ReadingStatus): EmptyResult<DataError.Firebase> {
        if (userId == null) return Result.Failure(DataError.Firebase.UNAUTHENTICATED)

        return try {
            firebaseDatabase.getReference("reading_list").child(userId!!).child(bookId).child("status").setValue(status).await()
            val currentList = readingListDao.getReadingList().first()
            val bookToUpdate = currentList.first { it.id == bookId }
            readingListDao.upsertBook(bookToUpdate.copy(status = status))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(DataError.Firebase.UNKNOWN)
        }
    }
}