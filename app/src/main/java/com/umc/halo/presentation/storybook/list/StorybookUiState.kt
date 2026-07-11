package com.umc.halo.presentation.storybook.list

import com.umc.halo.domain.model.storybook.CustomStorybook
import com.umc.halo.domain.model.storybook.StorybookTheme

/**
 * 스토리북 목록 화면 상태
 */
data class StorybookUiState(
    val userName: String = "",                                  // 맞춤 스토리북 제목에 쓰는 사용자 이름
    val selectedTab: StorybookTab = StorybookTab.ALL,           // 현재 선택된 탭
    val customStorybooks: List<CustomStorybook> = emptyList(),  // 맞춤 스토리북
    val themes: List<StorybookTheme> = emptyList()              // 상황별 테마 섹션들
)

/**
 * 전체/진행중/완료 탭
 */
enum class StorybookTab(val label: String) {
    ALL("전체"),
    IN_PROGRESS("진행중"),
    DONE("완료")
}
