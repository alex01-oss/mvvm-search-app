package com.loc.searchapp.domain.usecases.app_entry

import com.loc.searchapp.domain.manager.LocalUserManger

class SaveAppEntry(
    private val localUserManger: LocalUserManger
) {
    suspend operator fun invoke() {
        localUserManger.saveAppEntry()
    }
}