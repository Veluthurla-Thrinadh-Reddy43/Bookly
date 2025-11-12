package uk.ac.tees.mad.bookly.domain

import uk.ac.tees.mad.bookly.domain.util.DataError
import uk.ac.tees.mad.bookly.domain.util.EmptyResult

interface AuthRepository {
    suspend fun login(email: String, password: String): EmptyResult<DataError.Firebase>
    suspend fun signup(email: String, password: String): EmptyResult<DataError.Firebase>
    suspend fun sendPasswordResetEmail(email: String): EmptyResult<DataError.Firebase>
    suspend fun logout(): EmptyResult<DataError.Firebase>
}
