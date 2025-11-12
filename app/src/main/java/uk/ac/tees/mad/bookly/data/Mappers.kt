package uk.ac.tees.mad.bookly.data

import uk.ac.tees.mad.bookly.data.dtos.GoogleBooksResponse
import uk.ac.tees.mad.bookly.data.dtos.VolumeInfo
import uk.ac.tees.mad.bookly.data.local.BookEntity
import uk.ac.tees.mad.bookly.domain.Book

fun GoogleBooksResponse.toDomain(): List<Book> = items.orEmpty().mapNotNull { item ->
    item.volumeInfo?.toDomain(item.id)
}

fun VolumeInfo.toDomain(id: String): Book {
    val bestImage = imageLinks?.extraLarge ?: imageLinks?.large ?: imageLinks?.medium ?: imageLinks?.thumbnail
    return Book(
        id = id,
        title = title ?: "(Untitled)",
        authors = authors ?: emptyList(),
        description = description,
        thumbnail = bestImage,
        smallThumbnail = imageLinks?.smallThumbnail,
        publishDate = publishedDate,
        rating = averageRating
    )
}

fun Book.toEntity(query: String): BookEntity {
    return BookEntity(
        id = id,
        title = title,
        authors = authors.joinToString(", "),
        description = description ?: "",
        thumbnailUrl = thumbnail ?: "",
        searchQuery = query,
        publishDate = publishDate,
        rating = rating
    )
}

fun BookEntity.toBook(): Book {
    return Book(
        id = id,
        title = title,
        authors = authors.split(", ").map { it.trim() },
        description = description,
        thumbnail = thumbnailUrl,
        smallThumbnail = null, // Not stored in DB
        publishDate = publishDate,
        rating = rating
    )
}
