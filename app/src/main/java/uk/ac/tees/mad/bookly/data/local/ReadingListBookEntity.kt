package uk.ac.tees.mad.bookly.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import uk.ac.tees.mad.bookly.presentation.reading_list.ReadingStatus

@Entity(tableName = "reading_list")
data class ReadingListBookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val imageUrl: String,
    val status: ReadingStatus
)