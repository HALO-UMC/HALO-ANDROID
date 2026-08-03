package com.umc.halo.presentation.storybook.list

import com.umc.halo.domain.model.storybook.CustomStorybook
import com.umc.halo.domain.model.storybook.InProgressStorybook
import com.umc.halo.domain.model.storybook.Storybook
import com.umc.halo.domain.model.storybook.StorybookTheme

/**
 * 스토리북 목록 화면 상태
 *
 * @param isLoading 보여줄 게 아직 없는 첫 조회 중 (이미 목록이 있으면 화면을 비우지 않으려고 false 로)
 * @param hasLoadFailed 조회에 실패, 화면에 보여줄 목록 없음 → '다시 시도'
 * @param errorMessage 조회 실패 안내 문구
 */
data class StorybookUiState(
    val userName: String = "",                                  // 맞춤 스토리북 제목에 쓰는 사용자 이름
    val selectedTab: StorybookTab = StorybookTab.ALL,           // 현재 선택된 탭
    val customStorybooks: List<CustomStorybook> = emptyList(),  // 맞춤 스토리북
    val themes: List<StorybookTheme> = emptyList(),             // 상황별 테마 섹션들
    val inProgressStorybooks: List<InProgressStorybook> = emptyList(),  // 진행중 탭 목록
    val doneStorybooks: List<Storybook> = emptyList(),          // 완료 탭 목록 (10장까지 완료한 책만)
    val isLoading: Boolean = false,
    val hasLoadFailed: Boolean = false,
    val errorMessage: String? = null
)

/**
 * 전체/진행중/완료 탭
 */
enum class StorybookTab(val label: String) {
    ALL("전체"),
    IN_PROGRESS("진행중"),
    DONE("완료")
}
