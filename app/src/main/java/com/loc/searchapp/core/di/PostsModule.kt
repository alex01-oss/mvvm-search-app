package com.loc.searchapp.core.di

import com.loc.searchapp.core.data.remote.api.PostsApi
import com.loc.searchapp.core.data.repository.PostsRepositoryImpl
import com.loc.searchapp.core.domain.repository.PostsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class PostsModule {

    @Binds
    @Singleton
    abstract fun bindPostsRepository(
        repositoryImpl: PostsRepositoryImpl
    ): PostsRepository

    companion object {
        @Provides
        @Singleton
        fun providePostsApi(
            @MainRetrofit retrofit: Retrofit
        ): PostsApi {
            return retrofit.create(PostsApi::class.java)
        }
    }
}