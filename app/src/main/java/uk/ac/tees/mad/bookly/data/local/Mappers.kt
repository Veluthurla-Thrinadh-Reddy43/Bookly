package uk.ac.tees.mad.bookly.data.local

import uk.ac.tees.mad.bookly.domain.Book
import uk.ac.tees.mad.bookly.presentation.reading_list.ReadingListBook

fun ReadingListBookEntity.toReadingListBook(): ReadingListBook {
    return ReadingListBook(
        id = id,
        title = title,
        author = author,
        imageUrl = imageUrl,
        status = status
    )
}

fun ReadingListBook.toReadingListBookEntity(): ReadingListBookEntity {
    return ReadingListBookEntity(
        id = id,
        title = title,
        author = author,
        imageUrl = imageUrl,
        status = status
    )
}

fun BookEntity.toBook(): Book {
    return Book(
        id = id,
        title = title,
        authors = authors.split(",").map { it.trim() },
        description = description,
        thumbnail = thumbnailUrl,
        smallThumbnail = null,
        publishDate = publishDate,
        rating = rating
    )
}
