package com.umc.halo.data.repository.settings

import com.umc.halo.data.remote.api.settings.SettingsApi
import com.umc.halo.data.remote.dto.request.settings.BgmSettingUpdateRequest
import com.umc.halo.data.remote.dto.request.settings.NotificationSettingsUpdateRequest
import com.umc.halo.data.remote.dto.response.settings.BgmSettingResponse
import com.umc.halo.data.remote.dto.response.settings.NotificationSettingsResponse
import com.umc.halo.domain.model.settings.BgmSetting
import com.umc.halo.domain.model.settings.NotificationSettings
import com.umc.halo.domain.repository.settings.SettingsRepository
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsApi: SettingsApi
) : SettingsRepository {

    override suspend fun getNotificationSettings(): NotificationSettings {
        val result = settingsApi.getNotificationSettings().result
            ?: throw IllegalStateException("notification settings data is null")

        return result.toDomain()
    }

    override suspend fun updateNotificationSettings(settings: NotificationSettings): NotificationSettings {
        val result = settingsApi.updateNotificationSettings(
            NotificationSettingsUpdateRequest(
                isAllNotificationEnabled = settings.isAllNotificationEnabled,
                regularNotificationTime = settings.regularNotificationTime ?: DEFAULT_NOTIFICATION_TIME,
                todayChapterNotificationEnabled = settings.todayChapterNotificationEnabled,
                retentionNotificationEnabled = settings.retentionNotificationEnabled,
                anniversaryNotificationEnabled = settings.anniversaryNotificationEnabled
            )
        ).result ?: throw IllegalStateException("updated notification settings data is null")

        return result.toDomain()
    }

    override suspend fun getBgmSetting(): BgmSetting {
        val result = settingsApi.getBgmSetting().result
            ?: throw IllegalStateException("bgm setting data is null")

        return result.toDomain()
    }

    override suspend fun updateBgmSetting(setting: BgmSetting): BgmSetting {
        val result = settingsApi.updateBgmSetting(
            BgmSettingUpdateRequest(
                bgmId = setting.bgmId,
                bgmEnabled = setting.bgmEnabled,
                bgmVolume = setting.bgmVolume.coerceIn(0, 100)
            )
        ).result ?: throw IllegalStateException("updated bgm setting data is null")

        return result.toDomain()
    }
}

private fun NotificationSettingsResponse.toDomain(): NotificationSettings = NotificationSettings(
    isAllNotificationEnabled = isAllNotificationEnabled == true,
    regularNotificationTime = regularNotificationTime ?: DEFAULT_NOTIFICATION_TIME,
    todayChapterNotificationEnabled = todayChapterNotificationEnabled == true,
    retentionNotificationEnabled = retentionNotificationEnabled == true,
    anniversaryNotificationEnabled = anniversaryNotificationEnabled == true
)

private fun BgmSettingResponse.toDomain(): BgmSetting = BgmSetting(
    bgmId = bgmId ?: DEFAULT_BGM_ID,
    bgmEnabled = bgmEnabled == true,
    bgmVolume = bgmVolume?.coerceIn(0, 100) ?: DEFAULT_BGM_VOLUME
)

private const val DEFAULT_NOTIFICATION_TIME = "09:00"
private const val DEFAULT_BGM_ID = 1L
private const val DEFAULT_BGM_VOLUME = 50
