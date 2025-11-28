package uk.ac.tees.mad.bookly.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [BookEntity::class, ReadingListBookEntity::class],
    version = 3, // Incremented version
    exportSchema = false
)
abstract class BooklyDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun readingListDao(): ReadingListDao
}
