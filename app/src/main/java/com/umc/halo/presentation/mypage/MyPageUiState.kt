package com.umc.halo.presentation.mypage

import com.umc.halo.presentation.base.UiState

data class MyPageUiState(
    val bgmEnabled: Boolean = true,
    val volume: Float = 0.42f,
    val selectedTrackIndex: Int = 0,
    val playingTrackIndex: Int = 0,
    val allNotificationsEnabled: Boolean = true,
    val todayChapterNotificationEnabled: Boolean = true,
    val anniversaryNotificationEnabled: Boolean = true,
    val retentionNotificationEnabled: Boolean = true,
    val notificationHour: Int = 9,
    val notificationMinute: Int = 0,
    val isNotificationTimeConfigured: Boolean = false,
    val showNotificationTimeDialog: Boolean = false,
    val isEditingNotificationTime: Boolean = false,
    val showLogoutDialog: Boolean = false,
    val showWithdrawDialog: Boolean = false
) : UiState

fun MyPageUiState.formattedNotificationTime(): String {
    val hourText = notificationHour.toString().padStart(2, '0')
    val minuteText = if (notificationMinute == 0) {
        ""
    } else {
        " ${notificationMinute.toString().padStart(2, '0')}분"
    }

    return "${hourText}시$minuteText"
}
