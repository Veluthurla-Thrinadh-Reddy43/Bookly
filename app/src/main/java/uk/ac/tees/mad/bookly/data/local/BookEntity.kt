package uk.ac.tees.mad.bookly.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val authors: String,
    val description: String,
    val thumbnailUrl: String,
    val searchQuery: String,
    val publishDate: String?,
    val rating: Double?
)