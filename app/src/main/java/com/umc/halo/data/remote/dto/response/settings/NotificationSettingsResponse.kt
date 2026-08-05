package com.umc.halo.data.remote.dto.response.settings

data class NotificationSettingsResponse(
    val isAllNotificationEnabled: Boolean?,
    val regularNotificationTime: String?,
    val todayChapterNotificationEnabled: Boolean?,
    val retentionNotificationEnabled: Boolean?,
    val anniversaryNotificationEnabled: Boolean?
)
