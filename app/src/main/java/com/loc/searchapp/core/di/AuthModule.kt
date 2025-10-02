package com.loc.searchapp.core.di

import com.loc.searchapp.core.data.remote.api.AuthApi
import com.loc.searchapp.core.data.repository.AuthRepositoryImpl
import com.loc.searchapp.core.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        repositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    companion object {
        @Provides
        @Singleton
        fun provideAuthApi(
            @MainRetrofit retrofit: Retrofit
        ): AuthApi {
            return retrofit.create(AuthApi::class.java)
        }

        @Provides
        @Singleton
        @RefreshAuthApi
        fun provideRefreshAuthApi(
            @RefreshRetrofit retrofit: Retrofit
        ): AuthApi = retrofit.create(AuthApi::class.java)
    }
}