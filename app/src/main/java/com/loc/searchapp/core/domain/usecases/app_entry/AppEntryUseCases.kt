package com.loc.searchapp.core.domain.usecases.app_entry

import jakarta.inject.Inject

data class AppEntryUseCases @Inject constructor(
    val readAppEntry: ReadAppEntry,
    val saveAppEntry: SaveAppEntry
)
