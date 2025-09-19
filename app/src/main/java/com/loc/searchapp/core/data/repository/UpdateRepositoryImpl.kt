package com.loc.searchapp.core.data.repository

import android.content.Context
import android.os.Build
import com.loc.searchapp.core.data.remote.api.UpdateApi
import com.loc.searchapp.core.data.remote.dto.UpdateInfo
import com.loc.searchapp.core.domain.repository.UpdateRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UpdateRepositoryImpl @Inject constructor(
    private val api: UpdateApi,
    @ApplicationContext private val context: Context
) : UpdateRepository {

    override suspend fun checkForUpdate(): UpdateInfo? {
        return try {
            val res = api.getAppVersion()
            if (res.isSuccessful) {
                val updateDto = res.body()
                if (updateDto != null) {
                    val currentVersionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        context.packageManager
                            .getPackageInfo(context.packageName, 0)
                            .longVersionCode.toInt()
                    } else {
                        @Suppress("DEPRECATION")
                        context.packageManager
                            .getPackageInfo(context.packageName, 0)
                            .versionCode
                    }

                    if (updateDto.versionCode > currentVersionCode) {
                        updateDto
                    } else null
                } else null
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}