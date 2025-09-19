package com.loc.searchapp.core.domain.repository

import com.loc.searchapp.core.data.remote.dto.UpdateInfo

interface UpdateRepository {
    suspend fun checkForUpdate(): UpdateInfo?
}