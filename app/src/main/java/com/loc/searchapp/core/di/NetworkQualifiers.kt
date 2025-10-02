package com.loc.searchapp.core.di

import jakarta.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RefreshAuthApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RefreshClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RefreshRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class YoutubeApiKey