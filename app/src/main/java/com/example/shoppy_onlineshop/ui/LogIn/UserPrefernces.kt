import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

val Context.dataStore by preferencesDataStore(name = "user_prefs")

object UserPreferences {
    private val EMAIL_KEY = stringPreferencesKey("email")
    private val PASSWORD_KEY = stringPreferencesKey("password")

    fun saveCredentials(context: Context, email: String, password: String) {
        runBlocking {
            context.dataStore.edit { prefs ->
                prefs[EMAIL_KEY] = email
                prefs[PASSWORD_KEY] = password
            }
        }
    }

    fun getCredentials(context: Context): Pair<String?, String?> {
        return runBlocking {
            val prefs = context.dataStore.data.first()
            Pair(prefs[EMAIL_KEY], prefs[PASSWORD_KEY])
        }
    }

    fun clearCredentials(context: Context) {
        runBlocking {
            context.dataStore.edit { it.clear() }
        }
    }
}
