package com.loc.searchapp.core.data.remote.api

import com.loc.searchapp.core.data.remote.dto.UpdateInfo
import retrofit2.Response
import retrofit2.http.GET

interface UpdateApi {
    @GET("app/version")
    suspend fun getAppVersion(): Response<UpdateInfo>
}