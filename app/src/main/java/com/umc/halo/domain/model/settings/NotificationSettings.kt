package com.umc.halo.domain.model.settings

data class NotificationSettings(
    val isAllNotificationEnabled: Boolean,
    val regularNotificationTime: String?,
    val todayChapterNotificationEnabled: Boolean,
    val retentionNotificationEnabled: Boolean,
    val anniversaryNotificationEnabled: Boolean
) {
    val isReceiving: Boolean
        get() = isAllNotificationEnabled &&
                (todayChapterNotificationEnabled ||
                        retentionNotificationEnabled ||
                        anniversaryNotificationEnabled)
}
