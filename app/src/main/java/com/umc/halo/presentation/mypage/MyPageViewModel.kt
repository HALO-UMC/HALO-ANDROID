package com.umc.halo.presentation.mypage

import com.umc.halo.presentation.base.BaseViewModel

class MyPageViewModel : BaseViewModel<MyPageUiState, MyPageUiEvent>(
    initialState = MyPageUiState()
) {
    override fun onEvent(event: MyPageUiEvent) {
        when (event) {
            is MyPageUiEvent.BgmEnabledChanged -> updateState {
                copy(bgmEnabled = event.enabled)
            }

            is MyPageUiEvent.VolumeChanged -> updateState {
                copy(volume = event.volume.coerceIn(0f, 1f))
            }

            is MyPageUiEvent.TrackClicked -> updateState {
                copy(
                    selectedTrackIndex = event.index,
                    playingTrackIndex = event.index
                )
            }

            is MyPageUiEvent.AllNotificationsChanged -> updateState {
                copy(
                    allNotificationsEnabled = event.enabled,
                    todayChapterNotificationEnabled = event.enabled,
                    anniversaryNotificationEnabled = event.enabled,
                    retentionNotificationEnabled = event.enabled
                )
            }

            is MyPageUiEvent.TodayChapterNotificationChanged -> updateState {
                copy(todayChapterNotificationEnabled = event.enabled)
            }

            is MyPageUiEvent.AnniversaryNotificationChanged -> updateState {
                copy(anniversaryNotificationEnabled = event.enabled)
            }

            is MyPageUiEvent.RetentionNotificationChanged -> updateState {
                copy(retentionNotificationEnabled = event.enabled)
            }

            MyPageUiEvent.NotificationTimeClicked -> updateState {
                copy(
                    showNotificationTimeDialog = true,
                    isEditingNotificationTime = !isNotificationTimeConfigured
                )
            }

            MyPageUiEvent.NotificationTimeEditClicked -> updateState {
                copy(isEditingNotificationTime = true)
            }

            MyPageUiEvent.NotificationTimeDismissed -> updateState {
                copy(showNotificationTimeDialog = false)
            }

            is MyPageUiEvent.NotificationHourChanged -> updateState {
                copy(notificationHour = event.hour.coerceIn(0, 23))
            }

            is MyPageUiEvent.NotificationMinuteChanged -> updateState {
                copy(notificationMinute = event.minute.coerceIn(0, 59))
            }

            MyPageUiEvent.NotificationTimeConfirmed -> updateState {
                copy(
                    isNotificationTimeConfigured = true,
                    showNotificationTimeDialog = false,
                    isEditingNotificationTime = false
                )
            }

            is MyPageUiEvent.LogoutDialogChanged -> updateState {
                copy(showLogoutDialog = event.visible)
            }

            is MyPageUiEvent.WithdrawDialogChanged -> updateState {
                copy(showWithdrawDialog = event.visible)
            }
        }
    }
}
