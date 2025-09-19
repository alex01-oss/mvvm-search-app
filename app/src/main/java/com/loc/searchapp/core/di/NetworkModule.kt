package com.loc.searchapp.core.di

import android.content.Context
import com.loc.searchapp.core.data.remote.api.UpdateApi
import com.loc.searchapp.core.data.repository.UpdateRepositoryImpl
import com.loc.searchapp.core.domain.repository.UpdateRepository
import com.loc.searchapp.core.domain.usecases.update.CheckUpdate
import com.loc.searchapp.core.domain.usecases.update.UpdateUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideUpdateApiService(
        @Named("mainRetrofit")
        retrofit: Retrofit
    ): UpdateApi {
        return retrofit.create(UpdateApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUpdateRepository(
        api: UpdateApi,
        @ApplicationContext context: Context
    ): UpdateRepository {
        return UpdateRepositoryImpl(api, context)
    }

    @Provides
    @Singleton
    fun provideUpdateUseCases(repository: UpdateRepository): UpdateUseCases {
        return UpdateUseCases(
            checkUpdate = CheckUpdate(repository)
        )
    }
}