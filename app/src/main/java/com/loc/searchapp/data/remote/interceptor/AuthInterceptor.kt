package com.loc.searchapp.data.remote.interceptor

import android.util.Log
import com.loc.searchapp.data.local.preferences.UserPreferences
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val userPreferences: UserPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val accessToken = runBlocking {
            userPreferences.getAccessToken()
        }

        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Accept", "application/json")

        if (accessToken?.isNotBlank() == true) {
            requestBuilder.addHeader("Authorization", "Bearer $accessToken")
            Log.d("AuthInterceptor", "Adding token to request: ${accessToken.take(10)}...")
        } else {
            Log.d("AuthInterceptor", "No token available")
        }

        return chain.proceed(requestBuilder.build())
    }
}