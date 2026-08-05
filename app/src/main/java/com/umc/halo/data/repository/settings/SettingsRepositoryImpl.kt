package com.umc.halo.data.repository.settings

import com.umc.halo.data.remote.api.settings.SettingsApi
import com.umc.halo.data.remote.dto.request.settings.BgmSettingUpdateRequest
import com.umc.halo.data.remote.dto.response.settings.BgmSettingResponse
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

        return NotificationSettings(
            isAllNotificationEnabled = result.isAllNotificationEnabled == true,
            regularNotificationTime = result.regularNotificationTime,
            todayChapterNotificationEnabled = result.todayChapterNotificationEnabled == true,
            retentionNotificationEnabled = result.retentionNotificationEnabled == true,
            anniversaryNotificationEnabled = result.anniversaryNotificationEnabled == true
        )
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

private fun BgmSettingResponse.toDomain(): BgmSetting = BgmSetting(
    bgmId = bgmId ?: DEFAULT_BGM_ID,
    bgmEnabled = bgmEnabled == true,
    bgmVolume = bgmVolume?.coerceIn(0, 100) ?: DEFAULT_BGM_VOLUME
)

private const val DEFAULT_BGM_ID = 1L
private const val DEFAULT_BGM_VOLUME = 70
