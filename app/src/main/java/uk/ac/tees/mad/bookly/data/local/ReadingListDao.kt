package uk.ac.tees.mad.bookly.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBook(book: ReadingListBookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBooks(books: List<ReadingListBookEntity>)

    @Query("SELECT * FROM reading_list ORDER BY title ASC")
    fun getReadingList(): Flow<List<ReadingListBookEntity>> // Changed to return a Flow

    @Query("DELETE FROM reading_list WHERE id = :bookId")
    suspend fun deleteBook(bookId: String)

    @Query("DELETE FROM reading_list")
    suspend fun clearReadingList()
}
