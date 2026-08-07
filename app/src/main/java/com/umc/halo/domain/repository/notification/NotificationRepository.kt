package com.umc.halo.domain.repository.notification

interface NotificationRepository {
    suspend fun addMembers(fcmToken: String, deviceIdentifier: String)
    suspend fun deleteMembers(deviceIdentifier: String)

    suspend fun getFcmToken(): String
}