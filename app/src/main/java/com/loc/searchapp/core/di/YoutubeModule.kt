package com.loc.searchapp.core.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.loc.searchapp.BuildConfig
import com.loc.searchapp.core.data.remote.api.YoutubeApi
import com.loc.searchapp.core.data.repository.YoutubeRepositoryImpl
import com.loc.searchapp.core.domain.model.youtube.YoutubeData
import com.loc.searchapp.core.domain.repository.YoutubeRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class YoutubeModule {

    @Binds
    @Singleton
    abstract fun bindYoutubeRepository(
        impl: YoutubeRepositoryImpl
    ): YoutubeRepository

    companion object {

        @Provides
        @Singleton
        @YoutubeApiKey
        fun provideYoutubeApiKey(): String = BuildConfig.YOUTUBE_API_KEY

        @Provides
        @Singleton
        fun provideYoutubeApi(
            json: Json,
        ): YoutubeApi {
            return Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(YoutubeApi::class.java)
        }

        @Provides
        @Singleton
        fun provideYoutubeData(
            @YoutubeApiKey apiKey: String
        ): YoutubeData {
            return YoutubeData(
                apiKey = apiKey,
                playlistId = "UU3tUVI8r3Bfr8hb9-KzfCvw"
            )
        }
    }
}