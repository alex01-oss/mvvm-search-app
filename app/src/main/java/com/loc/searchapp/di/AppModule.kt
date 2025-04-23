package com.loc.searchapp.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.loc.searchapp.data.remote.interceptor.AuthInterceptor
import com.loc.searchapp.data.remote.interceptor.TokenRefreshInterceptor
import com.loc.searchapp.data.local.manager.LocalUserManagerImpl
import com.loc.searchapp.data.remote.api.AuthApi
import com.loc.searchapp.data.remote.api.CatalogApi
import com.loc.searchapp.data.local.preferences.UserPreferences
import com.loc.searchapp.data.local.preferences.dataStore
import com.loc.searchapp.data.repository.AuthRepositoryImpl
import com.loc.searchapp.data.repository.CatalogRepositoryImpl
import com.loc.searchapp.domain.manager.LocalUserManger
import com.loc.searchapp.domain.repository.AuthRepository
import com.loc.searchapp.domain.repository.CatalogRepository
import com.loc.searchapp.domain.usecases.app_entry.AppEntryUseCases
import com.loc.searchapp.domain.usecases.app_entry.ReadAppEntry
import com.loc.searchapp.domain.usecases.app_entry.SaveAppEntry
import com.loc.searchapp.domain.usecases.auth.AuthUseCases
import com.loc.searchapp.domain.usecases.auth.GetUser
import com.loc.searchapp.domain.usecases.auth.LoginUser
import com.loc.searchapp.domain.usecases.auth.LogoutUser
import com.loc.searchapp.domain.usecases.auth.RefreshToken
import com.loc.searchapp.domain.usecases.auth.RegisterUser
import com.loc.searchapp.domain.usecases.catalog.AddProduct
import com.loc.searchapp.domain.usecases.catalog.CatalogUseCases
import com.loc.searchapp.domain.usecases.catalog.DeleteProduct
import com.loc.searchapp.domain.usecases.catalog.GetCart
import com.loc.searchapp.domain.usecases.catalog.GetCatalogPaging
import com.loc.searchapp.domain.usecases.catalog.GetMenu
import com.loc.searchapp.domain.usecases.catalog.SelectProduct
import com.loc.searchapp.utils.Constants.CATALOG_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType


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
    @Named("authClient")
    fun provideAuthOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    @Singleton
    @Named("authRetrofit")
    fun provideAuthRetrofit(@Named("authClient") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(CATALOG_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(@Named("authRetrofit") retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMainOkHttpClient(
        authInterceptor: AuthInterceptor,
        tokenRefreshInterceptor: TokenRefreshInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(authInterceptor)
            .addInterceptor(tokenRefreshInterceptor)
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
    fun provideCatalogApi(okHttpClient: OkHttpClient): CatalogApi {
        return Retrofit.Builder()
            .baseUrl(CATALOG_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(CatalogApi::class.java)
    }

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
            selectProduct = SelectProduct(catalogRepository),
            getMenu = GetMenu(catalogRepository),
            getCatalogPaging = GetCatalogPaging(catalogRepository)
        )
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        authApi: AuthApi
    ): AuthRepository = AuthRepositoryImpl(authApi)

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
            getUser = GetUser(authRepository)
        )
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideUserPreferences(dataStore: DataStore<Preferences>): UserPreferences {
        return UserPreferences(dataStore)
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
