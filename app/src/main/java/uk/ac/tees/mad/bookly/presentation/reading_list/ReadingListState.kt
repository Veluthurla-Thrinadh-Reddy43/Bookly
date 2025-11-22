package uk.ac.tees.mad.bookly.presentation.reading_list


// Enum to represent the reading status of a book
enum class ReadingStatus {
    Finished,
    InProgress
}

// Represents a book in the user's personal reading list
data class ReadingListBook(
    val id: String,
    val title: String,
    val author: String,
    val imageUrl: String,
    val status: ReadingStatus
)

// Represents the overall state of the Reading List screen
data class ReadingListState(
    val totalBooks: Int = 0,
    val finishedBooks: Int = 0,
    val inProgressBooks: Int = 0,
    val books: List<ReadingListBook> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
