package com.loc.searchapp.core.data.remote.interceptor

import com.loc.searchapp.core.data.local.datastore.UserPreferences
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val userPreferences: UserPreferences
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestUrl = originalRequest.url.toString()

        if (shouldSkipAuth(requestUrl)) {
            return chain.proceed(originalRequest)
        }

        val accessToken = runBlocking { userPreferences.getAccessToken() }

        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")

        if (!accessToken.isNullOrBlank()) {
            requestBuilder.addHeader("Authorization", "Bearer $accessToken")
        }

        return chain.proceed(requestBuilder.build())
    }

    private fun shouldSkipAuth(url: String): Boolean {
        return url.contains("/auth/refresh") || url.contains("/auth/login")
    }
}
