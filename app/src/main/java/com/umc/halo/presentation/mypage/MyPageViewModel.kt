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
                    allNotificationsEnabled = event.enabled
                )
            }

            is MyPageUiEvent.TodayChapterNotificationChanged -> updateState {
                if (allNotificationsEnabled) {
                    copy(todayChapterNotificationEnabled = event.enabled)
                } else {
                    this
                }
            }

            is MyPageUiEvent.AnniversaryNotificationChanged -> updateState {
                if (allNotificationsEnabled) {
                    copy(anniversaryNotificationEnabled = event.enabled)
                } else {
                    this
                }
            }

            is MyPageUiEvent.RetentionNotificationChanged -> updateState {
                if (allNotificationsEnabled) {
                    copy(retentionNotificationEnabled = event.enabled)
                } else {
                    this
                }
            }

            MyPageUiEvent.NotificationTimeClicked -> updateState {
                if (allNotificationsEnabled) {
                    copy(
                        showNotificationTimeDialog = true,
                        isEditingNotificationTime = !isNotificationTimeConfigured,
                        draftNotificationHour = notificationHour,
                        draftNotificationMinute = notificationMinute
                    )
                } else {
                    this
                }
            }

            MyPageUiEvent.NotificationTimeEditClicked -> updateState {
                if (allNotificationsEnabled) {
                    copy(
                        isEditingNotificationTime = true,
                        draftNotificationHour = notificationHour,
                        draftNotificationMinute = notificationMinute
                    )
                } else {
                    this
                }
            }

            MyPageUiEvent.NotificationTimeDismissed -> updateState {
                copy(
                    showNotificationTimeDialog = false,
                    isEditingNotificationTime = false,
                    draftNotificationHour = notificationHour,
                    draftNotificationMinute = notificationMinute
                )
            }

            is MyPageUiEvent.NotificationHourChanged -> updateState {
                copy(draftNotificationHour = event.hour.coerceIn(0, 23))
            }

            is MyPageUiEvent.NotificationMinuteChanged -> updateState {
                copy(draftNotificationMinute = event.minute.coerceIn(0, 59))
            }

            MyPageUiEvent.NotificationHourIncreased -> updateState {
                copy(draftNotificationHour = (draftNotificationHour + 1) % 24)
            }

            MyPageUiEvent.NotificationHourDecreased -> updateState {
                copy(draftNotificationHour = (draftNotificationHour + 23) % 24)
            }

            MyPageUiEvent.NotificationMinuteIncreased -> updateState {
                copy(draftNotificationMinute = (draftNotificationMinute + 5) % 60)
            }

            MyPageUiEvent.NotificationMinuteDecreased -> updateState {
                copy(draftNotificationMinute = (draftNotificationMinute + 55) % 60)
            }

            MyPageUiEvent.NotificationTimeConfirmed -> updateState {
                copy(
                    notificationHour = draftNotificationHour,
                    notificationMinute = draftNotificationMinute,
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
