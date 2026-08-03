package com.umc.halo.presentation.storybook.list

/**
 * 스토리북 목록 화면 이벤트
 */
sealed interface StorybookUiEvent {
    // 전체/진행중/완료 탭 전환
    data class OnTabSelected(val tab: StorybookTab) : StorybookUiEvent

    /**
     * 화면이 보이기 시작 → 목록 조회
     */
    data object OnScreenShown : StorybookUiEvent

    // 조회 실패 화면의 '다시 시도'
    data object OnRetryClicked : StorybookUiEvent

    // 실패 토스트를 띄운 뒤 소비 (같은 메시지가 다시 뜨지 않도록)
    data object ErrorShown : StorybookUiEvent

    // 맞춤 스토리북 카드 클릭 → 스토리북 상세(목차)
    data class OnCustomStorybookClicked(val storybookId: Long) : StorybookUiEvent

    /**
     * 아직 시작 전(배지 없음) 스토리북 카드 클릭 → 스토리북 상세(목차)
     */
    data class OnStorybookClicked(val storybookId: Long) : StorybookUiEvent

    /**
     * 진행중인 스토리북 카드 클릭 → 스토리북 상세(목차)
     * 진행중 탭 카드 + 전체 탭의 "N장 진행중" 배지 카드 둘 다 이 이벤트 사용
     *
     * 목적지는 [OnStorybookClicked] 와 같지만 어떤 상태의 카드를 눌렀는지가 달라서 이벤트는 분리
     */
    data class OnContinueStorybookClicked(val storybookId: Long) : StorybookUiEvent

    /**
     * 완료한 스토리북 클릭 → 테마함
     * 완료 탭 카드 + 전체 탭의 "완료" 배지 카드 둘 다 이 이벤트를 사용
     */
    data class OnDoneStorybookClicked(val storybookId: Long) : StorybookUiEvent
}
