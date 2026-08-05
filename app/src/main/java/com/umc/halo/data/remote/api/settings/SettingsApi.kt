package com.umc.halo.data.remote.api.settings

import com.umc.halo.core.network.BaseResponse
import com.umc.halo.data.remote.dto.response.settings.NotificationSettingsResponse
import retrofit2.http.GET

interface SettingsApi {
    @GET("api/v1/settings/notifications")
    suspend fun getNotificationSettings(): BaseResponse<NotificationSettingsResponse>
}
