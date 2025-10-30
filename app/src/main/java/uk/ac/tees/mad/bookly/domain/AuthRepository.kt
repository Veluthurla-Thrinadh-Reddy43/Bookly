package uk.ac.tees.mad.bookly.domain

import uk.ac.tees.mad.bookly.domain.util.EmptyResult
import uk.ac.tees.mad.bookly.domain.util.DataError

interface AuthRepository {
    suspend fun login(email: String, password: String): EmptyResult<DataError.Firebase>
    suspend fun signup(email: String, password: String): EmptyResult<DataError.Firebase>
    suspend fun sendPasswordResetEmail(email: String): EmptyResult<DataError.Firebase>
    suspend fun logout(): EmptyResult<DataError.Firebase>
}