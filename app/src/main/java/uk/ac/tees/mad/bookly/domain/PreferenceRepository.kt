package uk.ac.tees.mad.bookly.domain

interface PreferenceRepository {
    fun getLastSearchQuery(): String
    fun saveLastSearchQuery(query: String)
}
