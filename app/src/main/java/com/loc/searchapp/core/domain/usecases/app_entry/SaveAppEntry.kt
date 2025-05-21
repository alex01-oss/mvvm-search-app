package com.loc.searchapp.core.domain.usecases.app_entry

import com.loc.searchapp.core.domain.manager.LocalUserManger

class SaveAppEntry(
    private val localUserManger: LocalUserManger
) {
    suspend operator fun invoke() {
        localUserManger.saveAppEntry()
    }
}