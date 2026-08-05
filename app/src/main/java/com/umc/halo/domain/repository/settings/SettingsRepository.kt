package com.umc.halo.domain.repository.settings

import com.umc.halo.domain.model.settings.NotificationSettings

interface SettingsRepository {
    suspend fun getNotificationSettings(): NotificationSettings
}
