package com.loc.searchapp.data.authenticator

import android.util.Log
import com.loc.searchapp.data.network.AuthApi
import com.loc.searchapp.data.preferences.UserPreferences
import com.loc.searchapp.utils.Constants.CATALOG_URL
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val userPreferences: UserPreferences
) : Authenticator {

    private val authApi: AuthApi by lazy {
        Retrofit.Builder()
            .baseUrl(CATALOG_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        Log.d("AuthAuthenticator", "401 detected. Attempting token refresh...")

        if (responseCount(response) >= 2) {
            Log.d("AuthAuthenticator", "Too many retries, aborting")
            return null
        }

        val refreshToken = runBlocking {
            userPreferences.getRefreshToken()
        } ?: run {
            Log.d("AuthAuthenticator", "No refresh token found")
            return null
        }

        Log.d("AuthAuthenticator", "Found refresh token: ${refreshToken.take(10)}...")

        val newToken = runBlocking {
            try {
                val refreshResponse = authApi.refresh("Bearer $refreshToken")
                Log.d("AuthAuthenticator", "Refresh response code: ${refreshResponse.code()}")

                if (refreshResponse.isSuccessful) {
                    val token = refreshResponse.body()?.token
                    Log.d("AuthAuthenticator", "Got new access token: ${token?.take(10)}...")
                    userPreferences.saveToken(token ?: "")
                    token
                } else {
                    Log.d("AuthAuthenticator", "Refresh failed: ${refreshResponse.errorBody()?.string()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("AuthAuthenticator", "Exception during refresh: ${e.message}", e)
                null
            }
        }

        return newToken?.let {
            Log.d("AuthAuthenticator", "Rebuilding request with new token")
            response.request.newBuilder()
                .header("Authorization", "Bearer $it")
                .build()
        }
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var res = response.priorResponse
        while (res != null) {
            count++
            res = res.priorResponse
        }
        return count
    }
}
