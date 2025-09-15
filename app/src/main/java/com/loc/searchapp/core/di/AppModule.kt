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
import com.loc.searchapp.core.domain.usecases.app_entry.AppEntryUseCases
import com.loc.searchapp.core.domain.usecases.app_entry.ReadAppEntry
import com.loc.searchapp.core.domain.usecases.app_entry.SaveAppEntry
import com.loc.searchapp.core.domain.usecases.auth.AuthUseCases
import com.loc.searchapp.core.domain.usecases.auth.DeleteUser
import com.loc.searchapp.core.domain.usecases.auth.GetUser
import com.loc.searchapp.core.domain.usecases.auth.LoginUser
import com.loc.searchapp.core.domain.usecases.auth.LogoutUser
import com.loc.searchapp.core.domain.usecases.auth.RefreshToken
import com.loc.searchapp.core.domain.usecases.auth.RegisterUser
import com.loc.searchapp.core.domain.usecases.auth.UpdateUser
import com.loc.searchapp.core.domain.usecases.catalog.AddProduct
import com.loc.searchapp.core.domain.usecases.catalog.CatalogUseCases
import com.loc.searchapp.core.domain.usecases.catalog.DeleteProduct
import com.loc.searchapp.core.domain.usecases.catalog.GetCart
import com.loc.searchapp.core.domain.usecases.catalog.GetCatalogItem
import com.loc.searchapp.core.domain.usecases.catalog.GetCatalogPaging
import com.loc.searchapp.core.domain.usecases.catalog.GetCategories
import com.loc.searchapp.core.domain.usecases.catalog.GetFilters
import com.loc.searchapp.core.domain.usecases.posts.CreatePost
import com.loc.searchapp.core.domain.usecases.posts.DeletePost
import com.loc.searchapp.core.domain.usecases.posts.EditPost
import com.loc.searchapp.core.domain.usecases.posts.GetAllPosts
import com.loc.searchapp.core.domain.usecases.posts.GetPost
import com.loc.searchapp.core.domain.usecases.posts.PostsUseCases
import com.loc.searchapp.core.domain.usecases.posts.UploadImage
import com.loc.searchapp.core.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.ConnectionSpec
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


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
    fun provideAuthRepository(
        authApi: AuthApi,
    ): AuthRepository = AuthRepositoryImpl(authApi)

    @Provides
    @Singleton
    fun provideCatalogRepository(
        catalogApi: CatalogApi,
    ): CatalogRepository = CatalogRepositoryImpl(catalogApi)

    @Provides
    @Singleton
    fun provideCatalogUseCases(
        catalogRepository: CatalogRepository,
    ): CatalogUseCases {
        return CatalogUseCases(
            getCart = GetCart(catalogRepository),
            addProduct = AddProduct(catalogRepository),
            deleteProduct = DeleteProduct(catalogRepository),
            getCatalogPaging = GetCatalogPaging(catalogRepository),
            getCatalogItem = GetCatalogItem(catalogRepository),
            getFilters = GetFilters(catalogRepository),
            getCategories = GetCategories(catalogRepository)
        )
    }

    @Provides
    @Singleton
    fun provideAuthUseCases(
        authRepository: AuthRepository
    ): AuthUseCases {
        return AuthUseCases(
            loginUser = LoginUser(authRepository),
            registerUser = RegisterUser(authRepository),
            refreshToken = RefreshToken(authRepository),
            logoutUser = LogoutUser(authRepository),
            getUser = GetUser(authRepository),
            updateUser = UpdateUser(authRepository),
            deleteUser = DeleteUser(authRepository)
        )
    }

    @Provides
    @Singleton
    fun providePostsRepository(
        postsApi: PostsApi
    ): PostsRepository = PostsRepositoryImpl(postsApi)

    @Provides
    @Singleton
    fun providePostsUseCases(
        postsRepository: PostsRepository,
    ): PostsUseCases {
        return PostsUseCases(
            createPost = CreatePost(postsRepository),
            editPost = EditPost(postsRepository),
            deletePost = DeletePost(postsRepository),
            uploadImage = UploadImage(postsRepository),
            getPost = GetPost(postsRepository),
            getAllPosts = GetAllPosts(postsRepository)
        )
    }

    @Provides
    @Singleton
    fun provideLocalUserManger(
        application: Application
    ): LocalUserManger = LocalUserManagerImpl(application)

    @Provides
    @Singleton
    fun provideAppEntryUseCases(
        localUserManger: LocalUserManger
    ) = AppEntryUseCases(
        readAppEntry = ReadAppEntry(localUserManger),
        saveAppEntry = SaveAppEntry(localUserManger)
    )
}