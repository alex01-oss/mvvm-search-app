package com.loc.searchapp.core.data.common

import retrofit2.Response

private inline fun <T, R> handleResponse(
    response: Response<T>,
    transform: (T) -> R,
    errorMessage: String
): R {
    return if (response.isSuccessful) {
        response.body()?.let(transform)
            ?: throw RuntimeException("$errorMessage: body is null")
    } else {
        throw RuntimeException("$errorMessage: ${response.code()} ${response.message()}")
    }
}