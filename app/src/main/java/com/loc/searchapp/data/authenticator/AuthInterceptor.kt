package com.loc.searchapp.data.authenticator

import android.util.Log
import com.loc.searchapp.data.preferences.UserPreferences
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val userPreferences: UserPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val token = runBlocking {
            userPreferences.getToken()
        }

        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Accept", "application/json")

        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
            Log.d("AuthInterceptor", "Adding token to request: ${token.take(10)}...")
        } else {
            Log.d("AuthInterceptor", "No token available")
        }

        return chain.proceed(requestBuilder.build())
    }
}