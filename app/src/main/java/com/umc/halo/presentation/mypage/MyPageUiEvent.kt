package com.umc.halo.presentation.mypage

import com.umc.halo.presentation.base.UiEvent

sealed interface MyPageUiEvent : UiEvent {
    data class BgmEnabledChanged(val enabled: Boolean) : MyPageUiEvent
    data class VolumeChanged(val volume: Float) : MyPageUiEvent
    data class TrackClicked(val index: Int) : MyPageUiEvent

    data class AllNotificationsChanged(val enabled: Boolean) : MyPageUiEvent
    data class TodayChapterNotificationChanged(val enabled: Boolean) : MyPageUiEvent
    data class AnniversaryNotificationChanged(val enabled: Boolean) : MyPageUiEvent
    data class RetentionNotificationChanged(val enabled: Boolean) : MyPageUiEvent

    data object NotificationTimeClicked : MyPageUiEvent
    data object NotificationTimeEditClicked : MyPageUiEvent
    data object NotificationTimeDismissed : MyPageUiEvent
    data class NotificationHourChanged(val hour: Int) : MyPageUiEvent
    data class NotificationMinuteChanged(val minute: Int) : MyPageUiEvent
    data object NotificationTimeConfirmed : MyPageUiEvent

    data class LogoutDialogChanged(val visible: Boolean) : MyPageUiEvent
    data class WithdrawDialogChanged(val visible: Boolean) : MyPageUiEvent
}
