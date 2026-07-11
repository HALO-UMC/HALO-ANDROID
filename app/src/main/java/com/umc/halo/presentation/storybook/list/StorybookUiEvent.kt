package com.umc.halo.presentation.storybook.list

/**
 * 스토리북 목록 화면 이벤트
 */
sealed interface StorybookUiEvent {
    // 전체/진행중/완료 탭 전환
    data class OnTabSelected(val tab: StorybookTab) : StorybookUiEvent

    // 맞춤 스토리북 카드 클릭
    data class OnCustomStorybookClicked(val storybookId: Int) : StorybookUiEvent

    // 테마 섹션의 스토리북 카드 클릭
    data class OnStorybookClicked(val storybookId: Int) : StorybookUiEvent

    // 진행중 탭 '이어하기'(오늘 진행 안 한 스토리북) 클릭 → 해당 장으로 이동
    data class OnContinueStorybookClicked(val storybookId: Int, val chapter: Int) : StorybookUiEvent
}
