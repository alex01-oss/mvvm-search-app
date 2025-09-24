package com.loc.searchapp.core.domain.usecases.app_entry

import com.loc.searchapp.core.domain.manager.LocalUserManger
import jakarta.inject.Inject

class SaveAppEntry @Inject constructor(
    private val localUserManger: LocalUserManger
) {
    suspend operator fun invoke() {
        localUserManger.saveAppEntry()
    }
}