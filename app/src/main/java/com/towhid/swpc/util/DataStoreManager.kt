package com.towhid.swpc.util
import io.jsonwebtoken.Jwts
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.towhid.swpc.data.models.LoginResponse
import com.towhid.swpc.data.models.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.util.Date

private val Context.dataStore by preferencesDataStore("user_prefs")

object TokenManager {
    private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    private val USER_ID_KEY = intPreferencesKey("user_id")
    private val GENERAL_TOKEN_KEY = stringPreferencesKey("general_token")
    private val USER_DATA_KEY = stringPreferencesKey("user_data")
    private val USER_ROLE_KEY = stringPreferencesKey("user_role")

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tokens")

    suspend fun saveTokens(context: Context, loginResponse: LoginResponse) {

        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = loginResponse.token
            preferences[USER_ID_KEY] = loginResponse.userId ?: -1 // Store -1 if userId is null
            preferences[GENERAL_TOKEN_KEY] = loginResponse.token
            preferences[USER_ROLE_KEY] = loginResponse.role ?: "" // Store empty string if role is null

            // Serialize the User object to a JSON string
            val gson = Gson()
            val userJson = gson.toJson(loginResponse.user)
            preferences[USER_DATA_KEY] = userJson ?: "" // Store empty string if userJson is null
        }

    }
    // Add functions to retrieve the saved data
    suspend fun getUserId(context: Context): Int? {
        return context.dataStore.data.map { preferences ->
            preferences[USER_ID_KEY]
        }.firstOrNull()

    }

    suspend fun getGeneralToken(context: Context): String? {
        return context.dataStore.data.map { preferences ->
            preferences[GENERAL_TOKEN_KEY]
        }.firstOrNull()

    }

    suspend fun getUserData(context: Context): User? {
        return context.dataStore.data.map { preferences ->
            val userJson = preferences[USER_DATA_KEY]
            if (userJson != null && userJson.isNotEmpty()) {
                Gson().fromJson(userJson, User::class.java)
            } else {
                null
            }
        }.firstOrNull()

    }

    suspend fun getUserRole(context: Context): String? {
        return context.dataStore.data.map { preferences ->
            preferences[USER_ROLE_KEY]
        }.firstOrNull()

    }
    suspend fun getAccessToken(context: Context): String? {
        return context.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN_KEY]
        }.firstOrNull()
    }

    suspend fun clearTokens(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }


    suspend fun isTokenValid(context: Context): Boolean {
        val token = getGeneralToken(context) ?: return false // Return false if token is null
        return try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Replace with your signing key retrieval
                .build()
                .parseClaimsJws(token)
                .body

            val expirationDate = claims.expiration
            expirationDate?.after(Date()) ?: false // Check if the expiration date is after the current date
        } catch (e: Exception) {
            // Token parsing or validation failed (e.g., invalid signature, malformed token)
            e.printStackTrace()
            false
        }
    }

    private fun getSigningKey(): ByteArray {
        // Replace "YOUR_SECRET_KEY" with your actual secret key
        val secretKeyString = "swpcwatepuriFySystem"
        return secretKeyString.toByteArray() // Or use Base64 decoding if your key is encoded
    }

}