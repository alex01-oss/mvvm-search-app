package com.loc.searchapp.core.data.remote.authenticator

import androidx.media3.common.util.Log
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

    companion object {
        private const val MAX_RETRY_COUNT = 2
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= MAX_RETRY_COUNT) {
            runBlocking { userPreferences.clearTokens() }
            return null
        }

        val refreshToken = runBlocking { userPreferences.getRefreshToken() }
        if (refreshToken.isNullOrBlank()) {
            runBlocking { userPreferences.clearTokens() }
            return null
        }

        synchronized(this) {
            return refreshTokenAndRetry(response, refreshToken)
        }
    }

    private fun refreshTokenAndRetry(response: Response, refreshToken: String): Request? {
        val currentToken = runBlocking { userPreferences.getAccessToken() }
        val requestToken = response.request.header("Authorization")?.removePrefix("Bearer ")

        if (currentToken != null && requestToken != currentToken) {
            return buildRequestWithToken(response.request, currentToken)
        }

        val newTokens = runBlocking {
            try {
                val result = refreshAuthApi.refresh(RefreshTokenRequest(refreshToken))
                if (result.isSuccessful) {
                    result.body()
                } else {
                    Log.w("TokenAuth", "Refresh failed: ${result.code()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("TokenAuth", "Refresh error", e)
                null
            }
        }

        return if (newTokens?.accessToken?.isNotBlank() == true) {
            runBlocking {
                userPreferences.saveTokens(
                    accessToken = newTokens.accessToken,
                    refreshToken = newTokens.refreshToken
                )
            }
            buildRequestWithToken(response.request, newTokens.accessToken)
        } else {
            runBlocking { userPreferences.clearTokens() }
            null
        }
    }

    private fun buildRequestWithToken(request: Request, token: String): Request {
        return request.newBuilder()
            .removeHeader("Authorization")
            .addHeader("Authorization", "Bearer $token")
            .build()
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var current = response.priorResponse
        while (current != null) {
            count++
            current = current.priorResponse
        }
        return count
    }
}