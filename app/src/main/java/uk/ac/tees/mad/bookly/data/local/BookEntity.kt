package uk.ac.tees.mad.bookly.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val authors: String, // Room can't store lists directly, so we'll join it to a string
    val description: String,
    val thumbnailUrl: String,
    val searchQuery: String // To associate the book with the search that found it
)