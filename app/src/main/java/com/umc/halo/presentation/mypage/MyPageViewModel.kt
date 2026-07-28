package com.umc.halo.presentation.mypage

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.umc.halo.data.remote.auth.GoogleLoginDataSource
import com.umc.halo.data.remote.auth.KakaoLoginDataSource
import com.umc.halo.domain.repository.auth.AuthRepository
import com.umc.halo.domain.repository.member.MemberRepository
import com.umc.halo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * TODO(마이페이지 담당): 로그인 담당(요시)이 로그아웃/회원탈퇴 처리를 추가함
 *  - 기존에는 확인 다이얼로그의 '확인'이 곧바로 로그인 화면으로 navigate 만 했는데
 *    그러면 서버 세션과 저장된 토큰이 그대로 남아 다음 앱 실행 때 자동 로그인이 됨
 *  - 그래서 LogoutConfirmed / WithdrawConfirmed 이벤트에서 서버 호출과 토큰 삭제를 하고
 *    끝나면 navigateToLogin 신호로 화면 이동을 알리도록 교체함
 *  - 이 때문에 생성자 주입이 생겨 @HiltViewModel 이 되었고
 *    MyPageScreen.kt 의 viewModel() 호출이 hiltViewModel() 로 바뀜
 *  - 그 외 로직은 수정하지 않음
 */
@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val memberRepository: MemberRepository,
    private val kakaoLoginDataSource: KakaoLoginDataSource,
    private val googleLoginDataSource: GoogleLoginDataSource
) : BaseViewModel<MyPageUiState, MyPageUiEvent>(
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

            MyPageUiEvent.LogoutConfirmed -> logout()
            is MyPageUiEvent.WithdrawConfirmed -> withdraw(event.context)

            MyPageUiEvent.AccountNavigationHandled -> updateState { copy(navigateToLogin = false) }
            MyPageUiEvent.AccountErrorShown -> updateState { copy(accountErrorMessage = null) }
        }
    }

    /**
     * 로그아웃 — 서버 refreshToken 무효화 + 로컬 토큰 삭제
     * 회원 정보와 기록은 그대로 남으므로 같은 계정으로 다시 로그인하면 바로 홈으로 이동
     */
    private fun logout() {
        if (currentState.isProcessingAccountAction) return

        viewModelScope.launch {
            updateState { copy(isProcessingAccountAction = true) }

            // 서버 호출이 실패해도 로컬 토큰은 지워짐 (AuthRepository.logout 참고)
            authRepository.logout()

            updateState {
                copy(
                    isProcessingAccountAction = false,
                    showLogoutDialog = false,
                    navigateToLogin = true
                )
            }
        }
    }

    /**
     * 회원 탈퇴 — 서버 계정/온보딩 상태 삭제 + 소셜 연결 해제
     *
     * 소셜 연결까지 끊는 이유: 안 끊으면 재가입할 때 계정 선택 창 없이 직전 계정으로 바로 붙음
     * 어떤 소셜로 가입했는지 앱이 들고 있지 않아 양쪽 모두 호출하고 해당 없는 쪽은 무시
     */
    private fun withdraw(context: Context) {
        if (currentState.isProcessingAccountAction) return

        viewModelScope.launch {
            updateState { copy(isProcessingAccountAction = true) }

            val success = memberRepository.withdraw()

            if (success) {
                kakaoLoginDataSource.unlink()
                googleLoginDataSource.clearCredentialState(context)
            }

            updateState {
                copy(
                    isProcessingAccountAction = false,
                    showWithdrawDialog = false,
                    navigateToLogin = success,
                    accountErrorMessage = if (success) null else WITHDRAW_FAILED_MESSAGE
                )
            }
        }
    }

    private companion object {
        // TODO: 에러 문구/표시 방식은 디자인 확정 후 교체
        const val WITHDRAW_FAILED_MESSAGE = "회원 탈퇴에 실패했어요. 잠시 후 다시 시도해 주세요."
    }
}
