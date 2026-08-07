package com.umc.halo.data.remote.dto.request.settings

data class NotificationSettingsUpdateRequest(
    val isAllNotificationEnabled: Boolean,
    val regularNotificationTime: String,
    val todayChapterNotificationEnabled: Boolean,
    val retentionNotificationEnabled: Boolean,
    val anniversaryNotificationEnabled: Boolean
)
