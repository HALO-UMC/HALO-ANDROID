package com.umc.halo.data.remote.dto.request.notification

data class NotificationRequest(
    val fcmToken: String,
    val deviceType: String = "ANDROID",
    val deviceIdentifier: String
)
