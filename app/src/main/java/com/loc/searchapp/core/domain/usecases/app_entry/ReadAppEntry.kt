package com.loc.searchapp.core.domain.usecases.app_entry

import com.loc.searchapp.core.domain.manager.LocalUserManger
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ReadAppEntry @Inject constructor(
    private val localUserManger: LocalUserManger
) {
    operator fun invoke(): Flow<Boolean> {
        return localUserManger.readAppEntry()
    }
}