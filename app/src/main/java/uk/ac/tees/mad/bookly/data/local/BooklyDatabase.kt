package uk.ac.tees.mad.bookly.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [BookEntity::class],
    version = 2 // Incremented version
)
abstract class BooklyDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}
