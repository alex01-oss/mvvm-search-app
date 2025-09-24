package com.loc.searchapp.core.data.common

sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error<T>(val message: String, val code: Int? = null) : Result<T>()
}