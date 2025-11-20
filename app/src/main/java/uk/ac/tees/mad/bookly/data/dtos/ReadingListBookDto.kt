package uk.ac.tees.mad.bookly.data.dtos

import kotlinx.serialization.Serializable
import uk.ac.tees.mad.bookly.presentation.reading_list.ReadingStatus

@Serializable
data class ReadingListBookDto(
    val id: String = "",
    val title: String = "",
    val author: String = "",
    val imageUrl: String = "",
    val status: ReadingStatus = ReadingStatus.InProgress,
    val timestamp: Long = System.currentTimeMillis()
)