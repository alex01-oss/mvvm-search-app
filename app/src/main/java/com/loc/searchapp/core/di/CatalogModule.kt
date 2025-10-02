package com.loc.searchapp.core.di

import com.loc.searchapp.core.data.remote.api.CatalogApi
import com.loc.searchapp.core.data.repository.CatalogRepositoryImpl
import com.loc.searchapp.core.domain.repository.CatalogRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class CatalogModule {

    @Binds
    @Singleton
    abstract fun bindCatalogRepository(
        repositoryImpl: CatalogRepositoryImpl
    ): CatalogRepository

    companion object {
        @Provides
        @Singleton
        fun provideCatalogApi(
            @MainRetrofit retrofit: Retrofit
        ): CatalogApi = retrofit.create(CatalogApi::class.java)
    }
}