package com.loc.searchapp.core.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.loc.searchapp.BuildConfig
import com.loc.searchapp.core.data.remote.api.YoutubeApi
import com.loc.searchapp.core.data.repository.YoutubeRepositoryImpl
import com.loc.searchapp.core.domain.model.youtube.YoutubeData
import com.loc.searchapp.core.domain.repository.YoutubeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object YoutubeModule {
    private val contentType = "application/json".toMediaType()

    @Provides
    @Named("youtube_api_key")
    fun provideYoutubeApiKey(): String = BuildConfig.YOUTUBE_API_KEY

    @Provides
    fun provideYoutubeApi(): YoutubeApi {
        val json = Json { ignoreUnknownKeys = true }

        return Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/youtube/v3/")
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(YoutubeApi::class.java)
    }

    @Provides
    fun provideYoutubeRepository(
        api: YoutubeApi,
        @Named("youtube_api_key") apiKey: String
    ): YoutubeRepository {
        val playlistId = "UU3tUVI8r3Bfr8hb9-KzfCvw"
        return YoutubeRepositoryImpl(
            api,
            YoutubeData(
                apiKey = apiKey,
                playlistId = playlistId
            )
        )
    }
}