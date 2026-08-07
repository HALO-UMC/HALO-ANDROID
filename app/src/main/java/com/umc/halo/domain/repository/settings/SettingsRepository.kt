package com.umc.halo.domain.repository.settings

import com.umc.halo.domain.model.settings.BgmSetting
import com.umc.halo.domain.model.settings.NotificationSettings

interface SettingsRepository {
    suspend fun getNotificationSettings(): NotificationSettings
    suspend fun updateNotificationSettings(settings: NotificationSettings): NotificationSettings
    suspend fun getBgmSetting(): BgmSetting
    suspend fun updateBgmSetting(setting: BgmSetting): BgmSetting
}
