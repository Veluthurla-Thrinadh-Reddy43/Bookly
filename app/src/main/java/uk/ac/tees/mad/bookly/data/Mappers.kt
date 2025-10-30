package uk.ac.tees.mad.bookly.data

import uk.ac.tees.mad.bookly.data.dtos.GoogleBooksResponse
import uk.ac.tees.mad.bookly.data.local.BookEntity
import uk.ac.tees.mad.bookly.domain.Book

fun GoogleBooksResponse.toDomain(): List<Book> = items.orEmpty().mapNotNull { item ->
    Book(
        id = item.id,
        title = item.volumeInfo?.title ?: "(Untitled)",
        authors = item.volumeInfo?.authors ?: emptyList(),
        description = item.volumeInfo?.description,
        thumbnail = item.volumeInfo?.imageLinks?.thumbnail
    )
}

fun Book.toEntity(query: String): BookEntity {
    return BookEntity(
        id = id,
        title = title,
        authors = authors.joinToString(", "),
        description = description ?: "",
        thumbnailUrl = thumbnail ?: "",
        searchQuery = query
    )
}

fun BookEntity.toBook(): Book {
    return Book(
        id = id,
        title = title,
        authors = authors.split(", ").map { it.trim() },
        description = description,
        thumbnail = thumbnailUrl
    )
}
