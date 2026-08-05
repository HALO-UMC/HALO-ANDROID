package com.umc.halo.presentation.mypage

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.umc.halo.core.audio.BgmPlayerController
import com.umc.halo.core.audio.BgmTrackCatalog
import com.umc.halo.data.remote.auth.GoogleLoginDataSource
import com.umc.halo.data.remote.auth.KakaoLoginDataSource
import com.umc.halo.domain.model.member.MemberInfo
import com.umc.halo.domain.model.settings.BgmSetting
import com.umc.halo.domain.repository.auth.AuthRepository
import com.umc.halo.domain.repository.member.MemberRepository
import com.umc.halo.domain.repository.settings.SettingsRepository
import com.umc.halo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
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
    private val settingsRepository: SettingsRepository,
    private val bgmPlayerController: BgmPlayerController,
    private val kakaoLoginDataSource: KakaoLoginDataSource,
    private val googleLoginDataSource: GoogleLoginDataSource
) : BaseViewModel<MyPageUiState, MyPageUiEvent>(
    initialState = MyPageUiState()
) {
    init {
        loadMyPageData()
    }

    override fun onEvent(event: MyPageUiEvent) {
        when (event) {
            MyPageUiEvent.SystemSettingsEntered -> loadBgmSetting()

            is MyPageUiEvent.BgmEnabledChanged -> onBgmEnabledChanged(event.enabled)

            is MyPageUiEvent.VolumeChanged -> onBgmVolumeChanged(event.volume)

            MyPageUiEvent.VolumeChangeFinished -> saveCurrentBgmSetting()

            is MyPageUiEvent.TrackClicked -> onBgmTrackClicked(event.index)

            MyPageUiEvent.SystemSettingsErrorShown -> updateState {
                copy(systemSettingsErrorMessage = null)
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
    private fun loadMyPageData() {
        viewModelScope.launch {
            updateState { copy(isMyPageLoading = true) }

            runCatching { memberRepository.getMyInfo() }
                .onSuccess { member ->
                    updateState {
                        copy(
                            memberName = member.name.ifBlank { "-" },
                            memberBirthDate = member.birthDate.toDisplayDate(),
                            memberProvider = member.provider.toProviderLabel(),
                            memberEmail = member.email?.takeIf { it.isNotBlank() } ?: "-",
                            memberCreatedAt = member.createdAt.toDisplayDate(),
                            memberCharacterImageUrl = member.characterImageUrl
                        )
                    }
                }

            runCatching { settingsRepository.getNotificationSettings() }
                .onSuccess { settings ->
                    val (hour, minute) = settings.regularNotificationTime.toHourMinute()
                    updateState {
                        copy(
                            allNotificationsEnabled = settings.isAllNotificationEnabled,
                            todayChapterNotificationEnabled = settings.todayChapterNotificationEnabled,
                            anniversaryNotificationEnabled = settings.anniversaryNotificationEnabled,
                            retentionNotificationEnabled = settings.retentionNotificationEnabled,
                            notificationHour = hour,
                            notificationMinute = minute,
                            draftNotificationHour = hour,
                            draftNotificationMinute = minute,
                            isReceivingNotification = settings.isReceiving
                        )
                    }
                }
                .onFailure {
                    updateState { copy(isReceivingNotification = false) }
                }

            updateState { copy(isMyPageLoading = false) }
        }
    }

    private fun loadBgmSetting() {
        viewModelScope.launch {
            updateState { copy(isBgmLoading = true, systemSettingsErrorMessage = null) }

            runCatching { settingsRepository.getBgmSetting() }
                .onSuccess { setting ->
                    applyBgmSetting(setting)
                }
                .onFailure {
                    val fallback = BgmSetting(
                        bgmId = BgmTrackCatalog.DEFAULT_BGM_ID,
                        bgmEnabled = false,
                        bgmVolume = DEFAULT_BGM_VOLUME
                    )
                    bgmPlayerController.applySettings(
                        bgmId = fallback.bgmId,
                        enabled = false,
                        volume = fallback.bgmVolume.toVolumeFloat()
                    )
                    updateState {
                        copy(
                            isBgmLoading = false,
                            bgmEnabled = fallback.bgmEnabled,
                            volume = fallback.bgmVolume.toVolumeFloat(),
                            selectedTrackIndex = BgmTrackCatalog.indexOf(fallback.bgmId),
                            playingTrackIndex = null,
                            systemSettingsErrorMessage = BGM_LOAD_FAILED_MESSAGE
                        )
                    }
                }
        }
    }

    private fun onBgmEnabledChanged(enabled: Boolean) {
        val state = currentState
        val track = BgmTrackCatalog.trackByIndex(state.selectedTrackIndex)
        val volume = state.volume.coerceIn(0f, 1f)

        if (enabled) {
            bgmPlayerController.play(track.id, volume)
        } else {
            bgmPlayerController.stop()
        }

        updateState {
            copy(
                bgmEnabled = enabled,
                playingTrackIndex = if (enabled) selectedTrackIndex else null
            )
        }
        saveCurrentBgmSetting()
    }

    private fun onBgmVolumeChanged(volume: Float) {
        val coercedVolume = volume.coerceIn(0f, 1f)
        bgmPlayerController.setVolume(coercedVolume)
        updateState { copy(volume = coercedVolume) }
    }

    private fun onBgmTrackClicked(index: Int) {
        val state = currentState
        val track = BgmTrackCatalog.trackByIndex(index)
        val isSameTrack = state.selectedTrackIndex == index
        val isPlayingSameTrack = state.playingTrackIndex == index

        if (isSameTrack) {
            if (isPlayingSameTrack) {
                bgmPlayerController.pause()
                updateState { copy(playingTrackIndex = null) }
            } else {
                bgmPlayerController.play(track.id, state.volume)
                updateState { copy(playingTrackIndex = index) }
            }
            return
        }

        bgmPlayerController.play(track.id, state.volume)
        updateState {
            copy(
                selectedTrackIndex = index,
                playingTrackIndex = index
            )
        }
        saveCurrentBgmSetting()
    }

    private fun saveCurrentBgmSetting() {
        val state = currentState
        val setting = BgmSetting(
            bgmId = BgmTrackCatalog.trackByIndex(state.selectedTrackIndex).id,
            bgmEnabled = state.bgmEnabled,
            bgmVolume = state.volume.toVolumeInt()
        )

        viewModelScope.launch {
            runCatching { settingsRepository.updateBgmSetting(setting) }
                .onSuccess { updatedSetting ->
                    applyBgmSetting(updatedSetting, syncPlayer = false)
                }
                .onFailure {
                    updateState {
                        copy(systemSettingsErrorMessage = BGM_SAVE_FAILED_MESSAGE)
                    }
                }
        }
    }

    private fun applyBgmSetting(
        setting: BgmSetting,
        syncPlayer: Boolean = true
    ) {
        val trackIndex = BgmTrackCatalog.indexOf(setting.bgmId)
        val volume = setting.bgmVolume.toVolumeFloat()

        if (syncPlayer) {
            bgmPlayerController.applySettings(
                bgmId = setting.bgmId,
                enabled = setting.bgmEnabled,
                volume = volume
            )
        }

        updateState {
            copy(
                isBgmLoading = false,
                bgmEnabled = setting.bgmEnabled,
                volume = volume,
                selectedTrackIndex = trackIndex,
                playingTrackIndex = if (setting.bgmEnabled && bgmPlayerController.isPlaying) {
                    trackIndex
                } else {
                    null
                }
            )
        }
    }

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
        const val DEFAULT_BGM_VOLUME = 70
        const val BGM_LOAD_FAILED_MESSAGE = "BGM 설정을 불러오지 못했어요."
        const val BGM_SAVE_FAILED_MESSAGE = "BGM 설정을 저장하지 못했어요."
    }
}

private fun String?.toDisplayDate(): String {
    val date = this
        ?.takeIf { it.length >= 10 }
        ?.substring(0, 10)
        ?: return "-"

    return date.replace("-", ".")
}

private fun String?.toProviderLabel(): String = when (this) {
    "KAKAO" -> "카카오 로그인"
    "GOOGLE" -> "구글 로그인"
    else -> "-"
}

private fun String?.toHourMinute(): Pair<Int, Int> {
    val parts = this?.split(":").orEmpty()
    val hour = parts.getOrNull(0)?.toIntOrNull()?.coerceIn(0, 23) ?: 9
    val minute = parts.getOrNull(1)?.toIntOrNull()?.coerceIn(0, 59) ?: 0
    return hour to minute
}

private fun Int.toVolumeFloat(): Float = coerceIn(0, 100) / 100f

private fun Float.toVolumeInt(): Int = (coerceIn(0f, 1f) * 100).roundToInt().coerceIn(0, 100)
