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
    val draftNotificationHour: Int = notificationHour,
    val draftNotificationMinute: Int = notificationMinute,
    val isNotificationTimeConfigured: Boolean = false,
    val showNotificationTimeDialog: Boolean = false,
    val isEditingNotificationTime: Boolean = false,
    val showLogoutDialog: Boolean = false,
    val showWithdrawDialog: Boolean = false,

    // ---- 계정 처리 ----
    /** 로그아웃/탈퇴 처리 중 (버튼 중복 탭 방지) */
    val isProcessingAccountAction: Boolean = false,
    /** 로그아웃/탈퇴가 끝나 로그인 화면으로 보내야 함을 알리는 1회성 신호 */
    val navigateToLogin: Boolean = false,
    /** 계정 처리 실패 안내 문구 */
    val accountErrorMessage: String? = null
) : UiState

fun MyPageUiState.formattedNotificationTime(): String {
    val isAm = notificationHour < 12
    val periodText = if (isAm) "오전" else "오후"
    val displayHour = when {
        notificationHour == 0 -> 12
        notificationHour > 12 -> notificationHour - 12
        else -> notificationHour
    }
    val minuteText = if (notificationMinute == 0) {
        ""
    } else {
        " ${notificationMinute}분"
    }

    return "$periodText ${displayHour}시$minuteText"
}
