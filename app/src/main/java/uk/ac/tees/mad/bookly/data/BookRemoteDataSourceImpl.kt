package uk.ac.tees.mad.bookly.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import uk.ac.tees.mad.bookly.data.dtos.GoogleBooksResponse
import uk.ac.tees.mad.bookly.domain.BookRemoteDataSource
import uk.ac.tees.mad.bookly.domain.util.DataError
import uk.ac.tees.mad.bookly.domain.util.Result
import uk.ac.tees.mad.bookly.domain.util.httpResult
import javax.inject.Inject

class BookRemoteDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient
) : BookRemoteDataSource {

    override suspend fun searchBooks(
        query: String,
        maxResults: Int,
        startIndex: Int
    ): Result<GoogleBooksResponse, DataError.Remote> {
        return httpResult {
            httpClient.get("https://www.googleapis.com/books/v1/volumes") {
                parameter("q", query)
                parameter("maxResults", maxResults)
                parameter("startIndex", startIndex)
            }.body()
        }
    }
}