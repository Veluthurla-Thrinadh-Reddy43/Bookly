package uk.ac.tees.mad.bookly.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<BookEntity>)

    @Query("SELECT * FROM books WHERE searchQuery = :query")
    suspend fun getBooksByQuery(query: String): List<BookEntity>

    @Query("SELECT * FROM books WHERE id = :bookId")
    suspend fun getBookById(bookId: String): BookEntity?

    @Query("DELETE FROM books WHERE searchQuery = :query")
    suspend fun deleteBooksByQuery(query: String)
}
