package uk.ac.tees.mad.bookly.domain

import uk.ac.tees.mad.bookly.domain.util.DataError
import uk.ac.tees.mad.bookly.domain.util.EmptyResult
import uk.ac.tees.mad.bookly.domain.util.Result

interface SearchHistoryRepository {
    suspend fun addSearchTerm(term: String): EmptyResult<DataError.Firebase>
    suspend fun getRecentQueries(limit: Int): Result<List<String>, DataError.Firebase>
}