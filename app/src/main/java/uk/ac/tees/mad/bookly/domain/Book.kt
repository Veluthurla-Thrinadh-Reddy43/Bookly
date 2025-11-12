package uk.ac.tees.mad.bookly.domain

data class Book(
    val id: String,
    val title: String,
    val authors: List<String>,
    val description: String?,
    val thumbnail: String?,
    val smallThumbnail: String?,
    val publishDate: String?,
    val rating: Double?
)