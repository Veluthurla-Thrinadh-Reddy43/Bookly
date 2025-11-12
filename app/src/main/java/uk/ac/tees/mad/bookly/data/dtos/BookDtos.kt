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
    val imageLinks: ImageLinks? = null,
    val publishedDate: String? = null,
    val averageRating: Double? = null
)

@Serializable
data class ImageLinks(
    val smallThumbnail: String? = null,
    val thumbnail: String? = null,
    val small: String? = null,
    val medium: String? = null,
    val large: String? = null,
    val extraLarge: String? = null
)