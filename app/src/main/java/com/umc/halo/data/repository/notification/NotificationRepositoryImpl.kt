package com.umc.halo.data.repository.notification

import com.umc.halo.data.remote.api.notification.NotificationApi
import com.umc.halo.data.remote.dto.request.notification.NotificationRequest
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationApi: NotificationApi
): NotificationApi {
    override suspend fun addMembers(fcmToken: String, deviceIdentifier: String) {
        notificationApi.addMembers(
            NotificationRequest(
                fcmToken,
                deviceType = "ANDROID",
                deviceIdentifier
            )
        )
    }

    override suspend fun deleteMembers(deviceIdentifier: String) {
        notificationApi.deleteMembers(deviceIdentifier)
    }

}