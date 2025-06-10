package com.loc.searchapp.core.domain.usecases.auth

import com.loc.searchapp.core.data.remote.dto.UpdateUserRequest
import com.loc.searchapp.core.data.remote.dto.UserResponse
import com.loc.searchapp.core.domain.repository.AuthRepository
import retrofit2.Response
import javax.inject.Inject

class UpdateUser @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        accessToken: String,
        fullname: String,
        email: String,
        phone: String,
        password: String?
    ): Response<UserResponse> {
        val updateUserRequest = UpdateUserRequest(
            fullname = fullname,
            email = email,
            phone = phone,
            password = password
        )
        return authRepository.updateUser("Bearer $accessToken", updateUserRequest)
    }
}