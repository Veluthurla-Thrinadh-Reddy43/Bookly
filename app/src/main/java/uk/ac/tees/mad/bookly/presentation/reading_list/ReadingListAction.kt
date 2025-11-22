package uk.ac.tees.mad.bookly.presentation.reading_list


sealed interface ReadingListAction {
    // When a user clicks on a book to see its details
    data class OnBookClicked(val bookId: String) : ReadingListAction
    // When a user clicks the edit icon for a book
    data class OnEditClicked(val bookId: String) : ReadingListAction
    // When a user clicks the delete icon for a book
    data class OnDeleteClicked(val bookId: String) : ReadingListAction
    // When a user clicks the notifications icon
    object OnNotificationsClicked : ReadingListAction
}
