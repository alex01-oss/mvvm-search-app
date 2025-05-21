package com.loc.searchapp.core.data.remote.authenticator

import com.loc.searchapp.core.data.local.datastore.UserPreferences
import com.loc.searchapp.core.data.remote.api.AuthApi
import com.loc.searchapp.core.data.remote.dto.RefreshTokenRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val userPreferences: UserPreferences,
    private val refreshAuthApi: AuthApi
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) {
            runBlocking { userPreferences.clearTokens() }
            return null
        }

        val refreshToken = runBlocking { userPreferences.getRefreshToken() }
        if (refreshToken.isNullOrBlank()) return null

        synchronized(this) {
            val currentToken = runBlocking { userPreferences.getAccessToken() }
            val requestToken = response.request.header("Authorization")?.removePrefix("Bearer ")

            if (currentToken != null && requestToken != currentToken) {
                return response.request.newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization", "Bearer $currentToken")
                    .build()
            }

            val newTokens = runBlocking {
                try {
                    val result = refreshAuthApi.refresh(RefreshTokenRequest(refreshToken))
                    if (result.isSuccessful) result.body() else null
                } catch (_: Exception) {
                    null
                }
            }

            if (newTokens == null || newTokens.accessToken.isBlank()) {
                runBlocking { userPreferences.clearTokens() }
                return null
            }

            runBlocking {
                userPreferences.saveTokens(
                    accessToken = newTokens.accessToken,
                    refreshToken = newTokens.refreshToken
                )
            }

            return response.request.newBuilder()
                .removeHeader("Authorization")
                .addHeader("Authorization", "Bearer ${newTokens.accessToken}")
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