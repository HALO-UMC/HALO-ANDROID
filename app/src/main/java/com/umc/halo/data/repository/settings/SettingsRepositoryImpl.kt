package com.umc.halo.data.repository.settings

import com.umc.halo.core.network.BaseResponse
import com.umc.halo.data.remote.api.settings.SettingsApi
import com.umc.halo.data.remote.dto.request.settings.BgmSettingUpdateRequest
import com.umc.halo.data.remote.dto.request.settings.NotificationSettingsUpdateRequest
import com.umc.halo.data.remote.dto.response.settings.BgmSettingResponse
import com.umc.halo.data.remote.dto.response.settings.NotificationSettingsResponse
import com.umc.halo.domain.model.settings.BgmSetting
import com.umc.halo.domain.model.settings.NotificationSettings
import com.umc.halo.domain.repository.settings.SettingsRepository
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsApi: SettingsApi
) : SettingsRepository {

    override suspend fun getNotificationSettings(): NotificationSettings {
        val response = runCatching {
            settingsApi.getNotificationSettings()
        }.getOrElse { throwable ->
            throw IllegalStateException(throwable.toApiErrorMessage("알림 설정을 불러오지 못했어요."))
        }
        val result = response.requireResult("알림 설정을 불러오지 못했어요.")

        return result.toDomain()
    }

    override suspend fun updateNotificationSettings(settings: NotificationSettings): NotificationSettings {
        val response = runCatching {
            settingsApi.updateNotificationSettings(
                NotificationSettingsUpdateRequest(
                    isAllNotificationEnabled = settings.isAllNotificationEnabled,
                    regularNotificationTime = settings.regularNotificationTime ?: DEFAULT_NOTIFICATION_TIME,
                    todayChapterNotificationEnabled = settings.todayChapterNotificationEnabled,
                    retentionNotificationEnabled = settings.retentionNotificationEnabled,
                    anniversaryNotificationEnabled = settings.anniversaryNotificationEnabled
                )
            )
        }.getOrElse { throwable ->
            throw IllegalStateException(throwable.toApiErrorMessage("알림 설정을 저장하지 못했어요."))
        }
        val result = response.requireResult("알림 설정을 저장하지 못했어요.")

        return result.toDomain()
    }

    override suspend fun getBgmSetting(): BgmSetting {
        val response = runCatching {
            settingsApi.getBgmSetting()
        }.getOrElse { throwable ->
            throw IllegalStateException(throwable.toApiErrorMessage("BGM 설정을 불러오지 못했어요."))
        }
        val result = response.requireResult("BGM 설정을 불러오지 못했어요.")

        return result.toDomain()
    }

    override suspend fun updateBgmSetting(setting: BgmSetting): BgmSetting {
        val response = runCatching {
            settingsApi.updateBgmSetting(
                BgmSettingUpdateRequest(
                    bgmId = setting.bgmId,
                    bgmEnabled = setting.bgmEnabled,
                    bgmVolume = setting.bgmVolume.coerceIn(0, 100)
                )
            )
        }.getOrElse { throwable ->
            throw IllegalStateException(throwable.toApiErrorMessage("BGM 설정을 저장하지 못했어요."))
        }
        val result = response.requireResult("BGM 설정을 저장하지 못했어요.")

        return result.toDomain()
    }

    private fun <T> BaseResponse<T>.requireResult(defaultMessage: String): T {
        if (!isSuccess || result == null) {
            error(toApiErrorMessage(defaultMessage))
        }

        return result
    }
}

private fun Throwable.toApiErrorMessage(defaultMessage: String): String =
    if (this is HttpException) {
        response()
            ?.errorBody()
            ?.string()
            ?.extractApiErrorMessage()
            ?: defaultMessage
    } else {
        message?.takeIf { it.isNotBlank() } ?: defaultMessage
    }

private fun BaseResponse<*>.toApiErrorMessage(defaultMessage: String): String =
    message.takeIf { it.isNotBlank() } ?: defaultMessage

private fun String.extractApiErrorMessage(): String? =
    runCatching {
        val json = JSONObject(this)
        val result = json.opt("result")
        when (result) {
            is JSONObject -> {
                val keys = result.keys()
                if (keys.hasNext()) {
                    result.optString(keys.next()).takeIf { it.isNotBlank() }
                } else {
                    null
                }
            }
            is String -> result.takeIf { it.isNotBlank() }
            else -> null
        } ?: json.optString("message").takeIf { it.isNotBlank() }
    }.getOrNull()

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
