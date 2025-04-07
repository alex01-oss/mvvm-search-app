package com.loc.searchapp.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.loc.searchapp.data.authenticator.AuthAuthenticator
import com.loc.searchapp.data.authenticator.AuthInterceptor
import com.loc.searchapp.data.manger.LocalUserMangerImpl
import com.loc.searchapp.data.network.AuthApi
import com.loc.searchapp.data.network.CatalogApi
import com.loc.searchapp.data.preferences.UserPreferences
import com.loc.searchapp.data.preferences.dataStore
import com.loc.searchapp.data.repository.AuthRepositoryImpl
import com.loc.searchapp.data.repository.CatalogRepositoryImpl
import com.loc.searchapp.domain.manger.LocalUserManger
import com.loc.searchapp.domain.repository.AuthRepository
import com.loc.searchapp.domain.repository.CatalogRepository
import com.loc.searchapp.domain.usecases.app_entry.AppEntryUseCases
import com.loc.searchapp.domain.usecases.app_entry.ReadAppEntry
import com.loc.searchapp.domain.usecases.app_entry.SaveAppEntry
import com.loc.searchapp.domain.usecases.auth.AuthUseCases
import com.loc.searchapp.domain.usecases.auth.LoginUser
import com.loc.searchapp.domain.usecases.auth.RefreshToken
import com.loc.searchapp.domain.usecases.auth.RegisterUser
import com.loc.searchapp.domain.usecases.catalog.AddProduct
import com.loc.searchapp.domain.usecases.catalog.CatalogUseCases
import com.loc.searchapp.domain.usecases.catalog.DeleteProduct
import com.loc.searchapp.domain.usecases.catalog.GetCart
import com.loc.searchapp.domain.usecases.catalog.GetCatalog
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
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthAuthenticator(
        userPreferences: UserPreferences,
    ): AuthAuthenticator {
        return AuthAuthenticator(userPreferences)
    }

    //  OKHTTPCLIENT
    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        authAuthenticator: AuthAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(authInterceptor)
            .authenticator(authAuthenticator)
            .apply {
                connectionSpecs(
                    listOf(
                        ConnectionSpec.MODERN_TLS,
                        ConnectionSpec.COMPATIBLE_TLS,
                        ConnectionSpec.CLEARTEXT
                    )
                )
            }
            .build()
    }

    //  ONBOARDING
    @Provides
    @Singleton
    fun provideLocalUserManger(
        application: Application
    ): LocalUserManger = LocalUserMangerImpl(application)

    @Provides
    @Singleton
    fun provideAppEntryUseCases(
        localUserManger: LocalUserManger
    ) = AppEntryUseCases(
        readAppEntry = ReadAppEntry(localUserManger),
        saveAppEntry = SaveAppEntry(localUserManger)
    )

    //  CATALOG
    @Provides
    @Singleton
    fun provideCatalogApi(okHttpClient: OkHttpClient): CatalogApi {
        return Retrofit.Builder()
            .baseUrl(CATALOG_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CatalogApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCatalogRepository(
        catalogApi: CatalogApi,
    ): CatalogRepository = CatalogRepositoryImpl(
        catalogApi
    )

    @Provides
    @Singleton
    fun provideCatalogUseCases(
        catalogRepository: CatalogRepository,
        userPreferences: UserPreferences
    ): CatalogUseCases {
        return CatalogUseCases(
            getCatalog = GetCatalog(catalogRepository),
            getCart = GetCart(catalogRepository, userPreferences),
            addProduct = AddProduct(catalogRepository),
            deleteProduct = DeleteProduct(catalogRepository),
            selectProduct = SelectProduct(catalogRepository)
        )
    }

    //  AUTHORIZATION
    @Provides
    @Singleton
    fun provideAuthApi(okHttpClient: OkHttpClient): AuthApi {
        return Retrofit.Builder()
            .baseUrl(CATALOG_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
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
            refreshToken = RefreshToken(authRepository)
        )
    }

//  DATASTORE
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
}