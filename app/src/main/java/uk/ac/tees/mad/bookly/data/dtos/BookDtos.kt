package uk.ac.tees.mad.bookly.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class GoogleBooksResponse(
    val items: List<BookItem>? = null
)

@Serializable
data class BookItem(
    val id: String,
    val volumeInfo: VolumeInfo? = null
)

@Serializable
data class VolumeInfo(
    val title: String? = null,
    val authors: List<String>? = null,
    val description: String? = null,
    val imageLinks: ImageLinks? = null
)

@Serializable
data class ImageLinks(
    val smallThumbnail: String? = null,
    val thumbnail: String? = null
)