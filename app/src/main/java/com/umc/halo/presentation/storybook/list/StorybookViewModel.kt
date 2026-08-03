package com.umc.halo.presentation.storybook.list

import androidx.lifecycle.viewModelScope
import com.umc.halo.domain.repository.member.MemberRepository
import com.umc.halo.domain.repository.storybook.StorybookRepository
import com.umc.halo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 스토리북 목록 ViewModel
 *
 */
@HiltViewModel
class StorybookViewModel @Inject constructor(
    private val storybookRepository: StorybookRepository,
    private val memberRepository: MemberRepository
) : BaseViewModel<StorybookUiState, StorybookUiEvent>(StorybookUiState()) {

    // 화면에 다시 들어와 조회가 겹칠 때 늦게 도착한 이전 응답이 새 결과를 덮어쓰지 않도록 이전 요청을 취소
    private var loadJob: Job? = null

    override fun onEvent(event: StorybookUiEvent) {
        when (event) {
            is StorybookUiEvent.OnTabSelected -> {
                updateState { copy(selectedTab = event.tab) }
            }

            StorybookUiEvent.OnScreenShown,
            StorybookUiEvent.OnRetryClicked -> load()

            StorybookUiEvent.ErrorShown -> updateState { copy(errorMessage = null) }

            // 아래 카드 클릭들은 화면 이동 이벤트 → StorybookScreen 이 NavGraph 콜백으로 처리
            is StorybookUiEvent.OnCustomStorybookClicked,     // → 스토리북 상세(목차)
            is StorybookUiEvent.OnStorybookClicked,           // → 스토리북 상세(목차)
            is StorybookUiEvent.OnContinueStorybookClicked,   // → 스토리북 상세(목차)
            is StorybookUiEvent.OnDoneStorybookClicked -> Unit // → 테마함
        }
    }

    /**
     * 화면에 필요한 값 3가지를 서버에서 받아옴
     */
    private fun load() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            // 이미 보여주고 있는 목록이 있으면(= 재진입) 화면을 비우지 않고 갱신
            val isFirstLoad = currentState.themes.isEmpty()
            updateState { copy(isLoading = isFirstLoad, errorMessage = null) }

            val listDeferred = async { runCatching { storybookRepository.getStorybookList() } }
            val recommendedDeferred = async { runCatching { storybookRepository.getRecommendedStorybooks() } }
            val memberDeferred = async { runCatching { memberRepository.getMyInfo() } }

            // 3탭의 목록 — 이것만 실패하면 보여줄 게 없으므로 실패 화면으로
            listDeferred.await()
                .onSuccess { result ->
                    updateState {
                        copy(
                            themes = result.themes,
                            inProgressStorybooks = result.inProgressStorybooks,
                            doneStorybooks = result.doneStorybooks,
                            hasLoadFailed = false
                        )
                    }
                }
                .onFailure {
                    updateState {
                        copy(
                            // 기존 목록이 남아 있으면 그대로 두고 토스트로만 알림
                            hasLoadFailed = themes.isEmpty(),
                            errorMessage = LOAD_FAILED_MESSAGE
                        )
                    }
                }

            // 맞춤 스토리북·사용자 이름은 부가 정보라 실패해도 화면은 띄우게 함
            recommendedDeferred.await().onSuccess { recommended ->
                updateState { copy(customStorybooks = recommended) }
            }
            memberDeferred.await().onSuccess { member ->
                updateState { copy(userName = member.name) }
            }

            updateState { copy(isLoading = false) }
        }
    }

    private companion object {
        // TODO: 에러 문구/표시 방식은 디자인 확정 후 교체
        const val LOAD_FAILED_MESSAGE = "스토리북을 불러오지 못했어요. 잠시 후 다시 시도해 주세요."
    }
}
