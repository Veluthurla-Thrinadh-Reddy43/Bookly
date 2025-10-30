package uk.ac.tees.mad.bookly.data.database.remote.dto


import kotlinx.serialization.Serializable

@Serializable
data class GoogleBooksResponse(val items: List<GBItem>? = null)

@Serializable
data class GBItem(val id: String? = null, val volumeInfo: GBVolume? = null)

@Serializable
data class GBVolume(
    val title: String? = null,
    val authors: List<String>? = null,
    val description: String? = null,
    val imageLinks: GBImageLinks? = null
)

@Serializable
data class GBImageLinks(val thumbnail: String? = null)
