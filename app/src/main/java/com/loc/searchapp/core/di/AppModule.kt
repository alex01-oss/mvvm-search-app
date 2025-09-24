package com.loc.searchapp.core.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.loc.searchapp.core.data.local.datastore.UserPreferences
import com.loc.searchapp.core.data.local.manager.LocalUserManagerImpl
import com.loc.searchapp.core.data.remote.api.AuthApi
import com.loc.searchapp.core.data.remote.api.CatalogApi
import com.loc.searchapp.core.data.remote.api.PostsApi
import com.loc.searchapp.core.data.remote.authenticator.TokenAuthenticator
import com.loc.searchapp.core.data.remote.interceptor.AuthInterceptor
import com.loc.searchapp.core.data.repository.AuthRepositoryImpl
import com.loc.searchapp.core.data.repository.CatalogRepositoryImpl
import com.loc.searchapp.core.data.repository.PostsRepositoryImpl
import com.loc.searchapp.core.domain.manager.LocalUserManger
import com.loc.searchapp.core.domain.repository.AuthRepository
import com.loc.searchapp.core.domain.repository.CatalogRepository
import com.loc.searchapp.core.domain.repository.PostsRepository
import com.loc.searchapp.core.utils.Constants.BASE_URL
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Named
import jakarta.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.ConnectionSpec
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val contentType = "application/json".toMediaType()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideUserPreferences(dataStore: DataStore<Preferences>): UserPreferences {
        return UserPreferences(dataStore)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(userPreferences: UserPreferences): AuthInterceptor {
        return AuthInterceptor(userPreferences)
    }

    @Provides
    @Singleton
    @Named("refreshClient")
    fun provideRefreshOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named("refreshRetrofit")
    fun provideRefreshRetrofit(@Named("refreshClient") client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    @Named("refreshAuthApi")
    fun provideRefreshAuthApi(@Named("refreshRetrofit") retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTokenAuthenticator(
        userPreferences: UserPreferences,
        @Named("refreshAuthApi") refreshAuthApi: AuthApi
    ): TokenAuthenticator {
        return TokenAuthenticator(userPreferences, refreshAuthApi)
    }

    @Provides
    @Singleton
    @Named("mainClient")
    fun provideMainOkHttpClient(
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .authenticator(tokenAuthenticator)
            .connectionSpecs(
                listOf(
                    ConnectionSpec.MODERN_TLS,
                    ConnectionSpec.COMPATIBLE_TLS,
                    ConnectionSpec.CLEARTEXT
                )
            )
            .build()
    }

    @Provides
    @Singleton
    @Named("mainRetrofit")
    fun provideMainRetrofit(@Named("mainClient") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(@Named("mainRetrofit") retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCatalogApi(@Named("mainRetrofit") retrofit: Retrofit): CatalogApi {
        return retrofit.create(CatalogApi::class.java)
    }

    @Provides
    @Singleton
    fun providePostsApi(@Named("mainRetrofit") retrofit: Retrofit): PostsApi {
        return retrofit.create(PostsApi::class.java)
    }

    @Provides
    @Singleton
    @Named("authClient")
    fun provideAuthOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named("authRetrofit")
    fun provideAuthRetrofit(@Named("authClient") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideLocalUserManger(
        application: Application
    ): LocalUserManger = LocalUserManagerImpl(application)

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class AuthModule {

        @Binds
        @Singleton
        abstract fun bindAuthRepository(
            repositoryImpl: AuthRepositoryImpl
        ): AuthRepository
    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class CatalogModule {

        @Binds
        @Singleton
        abstract fun bindCatalogRepository(
            repositoryImpl: CatalogRepositoryImpl
        ): CatalogRepository
    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class PostsModule {

        @Binds
        @Singleton
        abstract fun bindPostsRepository(
            repositoryImpl: PostsRepositoryImpl
        ): PostsRepository
    }
}