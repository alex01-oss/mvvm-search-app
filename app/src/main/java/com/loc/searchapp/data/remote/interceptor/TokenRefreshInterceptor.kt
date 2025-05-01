package com.loc.searchapp.data.remote.interceptor

import com.loc.searchapp.data.remote.api.AuthApi
import com.loc.searchapp.data.local.preferences.UserPreferences
import com.loc.searchapp.data.remote.dto.RefreshTokenRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenRefreshInterceptor @Inject constructor(
    private val userPreferences: UserPreferences,
    private val authApi: AuthApi
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val response = chain.proceed(originalRequest)

        if (response.code == 401) {
            val refreshToken = runBlocking {
                userPreferences.getRefreshToken()
            }

            val refreshResponse = runBlocking {
                try {
                    authApi.refresh(
                        RefreshTokenRequest(
                            refreshToken = refreshToken.toString()
                        )
                    )
                } catch (_: Exception) {
                    null
                }
            }

            if (refreshResponse?.isSuccessful == true) {
                val newAccessToken = refreshResponse.body()?.accessToken ?: return response

                runBlocking {
                    userPreferences.saveTokens(newAccessToken, refreshToken.toString())
                }

                val newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $newAccessToken")
                    .build()

                response.close()
                return chain.proceed(newRequest)
            } else {
                runBlocking {
                    userPreferences.clearTokens()
                }
                return response
            }
        }
        return response
    }
}