package uk.ac.tees.mad.bookly.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.bookly.data.dtos.SearchHistoryItem
import uk.ac.tees.mad.bookly.domain.SearchHistoryRepository
import uk.ac.tees.mad.bookly.domain.util.DataError
import uk.ac.tees.mad.bookly.domain.util.EmptyResult
import uk.ac.tees.mad.bookly.domain.util.Result
import javax.inject.Inject

class SearchHistoryRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) : SearchHistoryRepository {

    override suspend fun addSearchTerm(term: String): EmptyResult<DataError.Firebase> {
        val userId = firebaseAuth.currentUser?.uid
        if (userId == null) {
            return Result.Failure(DataError.Firebase.UNAUTHENTICATED)
        }

        return try {
            // setValue does not return a Task, so we don't await it.
            firebaseDatabase.getReference("search_history").child(userId).push().setValue(SearchHistoryItem(term))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(DataError.Firebase.UNKNOWN)
        }
    }

    override suspend fun getRecentQueries(limit: Int): Result<List<String>, DataError.Firebase> {
        val userId = firebaseAuth.currentUser?.uid
        if (userId == null) {
            return Result.Failure(DataError.Firebase.UNAUTHENTICATED)
        }

        return try {
            val snapshot = firebaseDatabase.getReference("search_history")
                .child(userId)
                .orderByChild("timestamp")
                .limitToLast(limit)
                .get()
                .await()

            val queries = snapshot.children.mapNotNull { it.getValue(SearchHistoryItem::class.java)?.query }.reversed()
            Result.Success(queries)
        } catch (e: Exception) {
            Result.Failure(DataError.Firebase.UNKNOWN)
        }
    }
}