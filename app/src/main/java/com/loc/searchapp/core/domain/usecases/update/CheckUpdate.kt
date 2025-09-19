package com.loc.searchapp.core.domain.usecases.update

import com.loc.searchapp.core.data.remote.dto.UpdateInfo
import com.loc.searchapp.core.domain.repository.UpdateRepository
import javax.inject.Inject

class CheckUpdate @Inject constructor(
    private val updateRepository: UpdateRepository
) {
    suspend operator fun invoke(): UpdateInfo? {
        return updateRepository.checkForUpdate()
    }
}