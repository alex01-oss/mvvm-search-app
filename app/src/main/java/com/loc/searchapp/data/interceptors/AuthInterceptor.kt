package com.loc.searchapp.data.interceptors

import com.loc.searchapp.data.preferences.UserPreferences
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val userPreferences: UserPreferences

) : Interceptor {
    private val refreshTokenLock = Any()

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val token = runBlocking { userPreferences.getToken() } ?: ""
        var request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        var response = chain.proceed(request)

        if (response.code == 401) {
            synchronized(refreshTokenLock) {
                response.close()

                val refreshToken = runBlocking { userPreferences.getRefreshToken() } ?: ""

                if(true) {
                    request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $refreshToken")
                        .build()

                    response = chain.proceed(request)
                }
            }
        }
        return response
    }
}