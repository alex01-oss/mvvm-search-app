package com.loc.searchapp.data.remote.authenticator

import com.loc.searchapp.data.local.preferences.UserPreferences
import com.loc.searchapp.data.remote.api.AuthApi
import com.loc.searchapp.data.remote.dto.RefreshTokenRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val userPreferences: UserPreferences,
    private val authApi: AuthApi
) : Authenticator {

    companion object {
        private const val MAX_RETRIES = 2
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= MAX_RETRIES) {
            return clearTokensAndReturnNull()
        }

        val refreshToken = runBlocking { userPreferences.getRefreshToken() }
        if (refreshToken.isNullOrBlank()) return clearTokensAndReturnNull()

        val refreshResponse = runBlocking {
            try {
                authApi.refresh(RefreshTokenRequest(refreshToken = refreshToken))
            } catch (_: Exception) {
                null
            }
        }

        return when {
            refreshResponse == null || !refreshResponse.isSuccessful -> clearTokensAndReturnNull()
            else -> {
                val newTokens = refreshResponse.body()
                if (newTokens?.accessToken.isNullOrBlank()) {
                    return clearTokensAndReturnNull()
                }

                runBlocking {
                    userPreferences.saveTokens(
                        accessToken = newTokens.accessToken,
                        refreshToken = newTokens.refreshToken
                    )
                }

                response.request.newBuilder()
                    .removeHeader("Authorization")
                    .header("Authorization", "Bearer ${newTokens.accessToken}")
                    .build()
            }
        }
    }

    private fun clearTokensAndReturnNull(): Request? {
        runBlocking { userPreferences.clearTokens() }
        return null
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
