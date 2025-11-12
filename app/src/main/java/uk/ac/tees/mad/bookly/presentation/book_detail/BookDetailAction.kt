package uk.ac.tees.mad.bookly.presentation.book_detail


sealed interface BookDetailAction {
    // When the user clicks the back arrow
    object OnBackClicked : BookDetailAction
    // When the user clicks the 'Add to List' FAB
    object OnAddToListClicked : BookDetailAction
    // When the user clicks on a similar book
    data class OnSimilarBookClicked(val bookId: String) : BookDetailAction
}
