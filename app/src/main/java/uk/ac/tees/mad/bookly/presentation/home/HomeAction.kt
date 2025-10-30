package uk.ac.tees.mad.bookly.presentation.home


sealed interface HomeAction {
    // When the user types in the search bar
    data class OnSearchQueryChanged(val query: String) : HomeAction
    // When the user submits the search
    object OnSearch : HomeAction
    // When the user clicks the notification bell
    object OnNotificationsClicked : HomeAction
    // When the user clicks on a specific book
    data class OnBookClicked(val bookId: String) : HomeAction
}
