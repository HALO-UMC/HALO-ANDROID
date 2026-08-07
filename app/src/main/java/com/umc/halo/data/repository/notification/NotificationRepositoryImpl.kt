package com.umc.halo.data.repository.notification

import com.google.firebase.messaging.FirebaseMessaging
import com.umc.halo.data.remote.api.notification.NotificationApi
import com.umc.halo.data.remote.dto.request.notification.NotificationRequest
import com.umc.halo.domain.repository.notification.NotificationRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationApi: NotificationApi
): NotificationRepository {
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

    override suspend fun getFcmToken(): String {
        return FirebaseMessaging.getInstance().token.await()
    }

}