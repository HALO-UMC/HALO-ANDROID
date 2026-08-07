package com.umc.halo.presentation.mypage

import android.content.Context
import com.umc.halo.presentation.base.UiEvent

sealed interface MyPageUiEvent : UiEvent {
    data object SystemSettingsEntered : MyPageUiEvent
    data class BgmEnabledChanged(val enabled: Boolean) : MyPageUiEvent
    data class VolumeChanged(val volume: Float) : MyPageUiEvent
    data object VolumeChangeFinished : MyPageUiEvent
    data class TrackClicked(val index: Int) : MyPageUiEvent
    data object SystemSettingsErrorShown : MyPageUiEvent

    data object NotificationSettingsEntered : MyPageUiEvent
    data object NotificationSettingsErrorShown : MyPageUiEvent
    data class AllNotificationsChanged(val enabled: Boolean) : MyPageUiEvent
    data class TodayChapterNotificationChanged(val enabled: Boolean) : MyPageUiEvent
    data class AnniversaryNotificationChanged(val enabled: Boolean) : MyPageUiEvent
    data class RetentionNotificationChanged(val enabled: Boolean) : MyPageUiEvent

    data object NotificationTimeClicked : MyPageUiEvent
    data object NotificationTimeEditClicked : MyPageUiEvent
    data object NotificationTimeDismissed : MyPageUiEvent
    data class NotificationHourChanged(val hour: Int) : MyPageUiEvent
    data class NotificationMinuteChanged(val minute: Int) : MyPageUiEvent
    data object NotificationHourIncreased : MyPageUiEvent
    data object NotificationHourDecreased : MyPageUiEvent
    data object NotificationMinuteIncreased : MyPageUiEvent
    data object NotificationMinuteDecreased : MyPageUiEvent
    data object NotificationTimeConfirmed : MyPageUiEvent

    data class LogoutDialogChanged(val visible: Boolean) : MyPageUiEvent
    data class WithdrawDialogChanged(val visible: Boolean) : MyPageUiEvent

    // ---- 계정 처리 ----
    // 확인 다이얼로그의 '로그아웃 하기' → 서버 로그아웃 + 로컬 토큰 삭제
    data object LogoutConfirmed : MyPageUiEvent

    // 확인 다이얼로그의 '탈퇴하기' → 서버 탈퇴 + 소셜 연결 해제 (해제에 Activity Context 필요)
    data class WithdrawConfirmed(val context: Context) : MyPageUiEvent

    // 로그인 화면으로 이동을 처리한 뒤 1회성 신호 내리기
    data object AccountNavigationHandled : MyPageUiEvent

    // 에러 안내를 표시한 뒤 신호 내리기
    data object AccountErrorShown : MyPageUiEvent
}
