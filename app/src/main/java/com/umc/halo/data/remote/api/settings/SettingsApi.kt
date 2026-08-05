package com.umc.halo.data.remote.api.settings

import com.umc.halo.core.network.BaseResponse
import com.umc.halo.data.remote.dto.request.settings.BgmSettingUpdateRequest
import com.umc.halo.data.remote.dto.response.settings.BgmSettingResponse
import com.umc.halo.data.remote.dto.response.settings.NotificationSettingsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface SettingsApi {
    @GET("api/v1/settings/notifications")
    suspend fun getNotificationSettings(): BaseResponse<NotificationSettingsResponse>

    @GET("api/v1/settings/bgm")
    suspend fun getBgmSetting(): BaseResponse<BgmSettingResponse>

    @PUT("api/v1/settings/bgm")
    suspend fun updateBgmSetting(
        @Body request: BgmSettingUpdateRequest
    ): BaseResponse<BgmSettingResponse>
}
