package uk.ac.tees.mad.bookly.data.dtos

data class SearchHistoryItem(
    val query: String = "",
    val timestamp: Long = System.currentTimeMillis()
)