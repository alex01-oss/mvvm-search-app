package com.loc.searchapp.core.di

import com.loc.searchapp.core.data.local.manager.LocalUserManagerImpl
import com.loc.searchapp.core.domain.manager.LocalUserManger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserModule {

    @Binds
    @Singleton
    abstract fun bindLocalUserManager(
        impl: LocalUserManagerImpl
    ): LocalUserManger
}