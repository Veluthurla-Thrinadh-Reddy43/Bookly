package uk.ac.tees.mad.bookly.data

import android.content.SharedPreferences
import uk.ac.tees.mad.bookly.domain.PreferenceRepository
import javax.inject.Inject

class PreferenceRepositoryImpl @Inject constructor(
    private val prefs: SharedPreferences
) : PreferenceRepository {

    override fun getLastSearchQuery(): String {
        return prefs.getString(KEY_LAST_SEARCH, "") ?: ""
    }

    override fun saveLastSearchQuery(query: String) {
        prefs.edit().putString(KEY_LAST_SEARCH, query).apply()
    }

    companion object {
        private const val KEY_LAST_SEARCH = "last_search"
    }
}