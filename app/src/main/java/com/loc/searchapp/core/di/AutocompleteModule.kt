package com.loc.searchapp.core.di

import com.loc.searchapp.core.data.remote.api.AutocompleteApi
import com.loc.searchapp.core.data.repository.AutocompleteRepositoryImpl
import com.loc.searchapp.core.domain.repository.AutocompleteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class AutocompleteModule {

    @Binds
    @Singleton
    abstract fun bindAutocompleteRepository(
        repositoryImpl: AutocompleteRepositoryImpl
    ): AutocompleteRepository

    companion object {
        @Provides
        @Singleton
        fun provideAutocompleteApi(
            @MainRetrofit retrofit: Retrofit
        ): AutocompleteApi {
            return retrofit.create(AutocompleteApi::class.java)
        }
    }
}
