package uk.ac.tees.mad.bookly.domain


import uk.ac.tees.mad.bookly.domain.util.DataError
import uk.ac.tees.mad.bookly.domain.util.Result

interface BookSearchRepository {
    suspend fun search(
        query: String,
        maxResults: Int = 20,
        startIndex: Int = 0
    ): Result<List<Book>, DataError.Remote>

    suspend fun recentQueries(limit: Int = 10): Result<List<String>, DataError.Remote>
}