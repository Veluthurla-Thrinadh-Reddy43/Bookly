package uk.ac.tees.mad.bookly.data

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.bookly.domain.AuthRepository
import uk.ac.tees.mad.bookly.domain.util.DataError
import uk.ac.tees.mad.bookly.domain.util.EmptyResult
import uk.ac.tees.mad.bookly.domain.util.Result
import uk.ac.tees.mad.bookly.domain.util.firebaseEmpty
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    override suspend fun login(email: String, password: String): EmptyResult<DataError.Firebase> {
        return firebaseEmpty { auth.signInWithEmailAndPassword(email, password).await() }
    }

    override suspend fun signup(email: String, password: String): EmptyResult<DataError.Firebase> {
        return firebaseEmpty { auth.createUserWithEmailAndPassword(email, password).await() }
    }

    override suspend fun sendPasswordResetEmail(email: String): EmptyResult<DataError.Firebase> {
        return firebaseEmpty { auth.sendPasswordResetEmail(email).await() }
    }

    override suspend fun logout(): EmptyResult<DataError.Firebase> {
        return try {
            auth.signOut()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(DataError.Firebase.UNKNOWN)
        }
    }
}