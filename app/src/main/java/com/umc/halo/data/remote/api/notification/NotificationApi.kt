package com.umc.halo.data.remote.api.notification

import com.umc.halo.core.network.BaseResponse
import com.umc.halo.data.remote.dto.request.notification.NotificationRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationApi {

    @POST("/api/v1/members/devices")
    suspend fun addMembers(@Body request: NotificationRequest)

    @DELETE("/api/v1/members/devices")
    suspend fun deleteMembers(@Query("deviceIdentifier") deviceIdentifier: String)
}