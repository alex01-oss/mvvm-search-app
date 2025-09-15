package com.loc.searchapp.core.data.remote.interceptor

import com.loc.searchapp.core.data.local.datastore.UserPreferences
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val userPreferences: UserPreferences
) : Interceptor {

    companion object {
        private val AUTH_SKIP_ENDPOINTS = setOf("/auth/refresh", "/auth/login", "/auth/register")
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        if (shouldSkipAuth(originalRequest.url.toString())) {
            return chain.proceed(originalRequest)
        }

        val accessToken = runBlocking { userPreferences.getAccessToken() }
        val request = originalRequest.newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .apply {
                accessToken?.let { addHeader("Authorization", "Bearer $it") }
            }
            .build()

        return chain.proceed(request)
    }

    private fun shouldSkipAuth(url: String): Boolean {
        return AUTH_SKIP_ENDPOINTS.any { url.contains(it) }
    }
}