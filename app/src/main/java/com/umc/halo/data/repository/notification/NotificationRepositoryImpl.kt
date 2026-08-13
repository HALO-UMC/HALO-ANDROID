package com.umc.halo.data.repository.notification

import com.google.firebase.messaging.FirebaseMessaging
import com.umc.halo.core.network.toApiErrorMessage
import com.umc.halo.data.remote.api.notification.NotificationApi
import com.umc.halo.data.remote.dto.request.notification.NotificationRequest
import com.umc.halo.domain.repository.notification.NotificationRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationApi: NotificationApi
): NotificationRepository {
    override suspend fun addMembers(fcmToken: String, deviceIdentifier: String) {
        val response = notificationApi.addMembers(
            NotificationRequest(
                fcmToken,
                deviceType = "ANDROID",
                deviceIdentifier
            )
        )

        if (!response.isSuccess) {
            // 호출부(로그인, 로그아웃)가 실패해도 흐름을 막지 않고 로그만 남김
            error(response.toApiErrorMessage(REGISTER_FAILED_MESSAGE))
        }
    }

    override suspend fun deleteMembers(deviceIdentifier: String) {
        notificationApi.deleteMembers(deviceIdentifier)
    }

    override suspend fun getFcmToken(): String {
        return FirebaseMessaging.getInstance().token.await()
    }

    private companion object {
        const val REGISTER_FAILED_MESSAGE = "기기 등록에 실패했어요."
    }
}