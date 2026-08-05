package com.umc.halo.data.repository.settings

import com.umc.halo.data.remote.api.settings.SettingsApi
import com.umc.halo.domain.model.settings.NotificationSettings
import com.umc.halo.domain.repository.settings.SettingsRepository
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsApi: SettingsApi
) : SettingsRepository {

    override suspend fun getNotificationSettings(): NotificationSettings {
        val result = settingsApi.getNotificationSettings().result
            ?: throw IllegalStateException("notification settings data is null")

        return NotificationSettings(
            isAllNotificationEnabled = result.isAllNotificationEnabled == true,
            regularNotificationTime = result.regularNotificationTime,
            todayChapterNotificationEnabled = result.todayChapterNotificationEnabled == true,
            retentionNotificationEnabled = result.retentionNotificationEnabled == true,
            anniversaryNotificationEnabled = result.anniversaryNotificationEnabled == true
        )
    }
}
