package com.umc.halo.presentation.storybook.list

import com.umc.halo.domain.model.storybook.CustomStorybook
import com.umc.halo.domain.model.storybook.InProgressStorybook
import com.umc.halo.domain.model.storybook.Storybook
import com.umc.halo.domain.model.storybook.StorybookProgress
import com.umc.halo.domain.model.storybook.StorybookTheme
import com.umc.halo.presentation.base.BaseViewModel

/**
 * 스토리북 목록 ViewModel
 * 네트워크 연동 시점에 Repository 를 주입하면서 @HiltViewModel 로 승격 예정
 */
class StorybookViewModel : BaseViewModel<StorybookUiState, StorybookUiEvent>(StorybookUiState()) {

    override fun onEvent(event: StorybookUiEvent) {
        when (event) {
            is StorybookUiEvent.OnTabSelected -> {
                updateState { copy(selectedTab = event.tab) }
            }

            is StorybookUiEvent.OnCustomStorybookClicked -> {
                // TODO: 스토리북 상세(목차)로 navigation
            }

            is StorybookUiEvent.OnStorybookClicked -> {
                // TODO: 스토리북 상세(목차)로 navigation
            }

            is StorybookUiEvent.OnContinueStorybookClicked -> {
                // TODO: 해당 스토리북의 event.chapter 장(챕터 진행 화면)으로 navigation
            }

            is StorybookUiEvent.OnDoneStorybookClicked -> {
                // TODO: 해당 스토리북의 테마함으로 navigation
            }
        }
    }

    init {
        // TODO: 서버 연동 전까지 더미 데이터로 처리
        updateState {
            copy(
                userName = "주연",
                customStorybooks = listOf(
                    CustomStorybook(1, "대화가 어색한 당신을 위한", "오래전 당신", "가족과의 만남"),
                    CustomStorybook(2, "특별한 날을 준비하고 싶은 당신을 위한", "생신까지 열 장", "가족과의 만남")
                ),
                themes = listOf(
                    StorybookTheme(
                        id = 1,
                        title = "대화가 어색할 때",
                        storybooks = listOf(
                            Storybook(11, "오래전 당신", "가족과의 만남", StorybookProgress.InProgress(4)),
                            Storybook(12, "한 장의 가족사진", "가족과의 만남", StorybookProgress.Done)
                        )
                    ),
                    StorybookTheme(
                        id = 2,
                        title = "부모님을 더 알고싶을 때",
                        storybooks = listOf(
                            Storybook(21, "가족의 온도", "가족과의 만남"),
                            Storybook(22, "당신 사용 설명서", "가족과의 만남", StorybookProgress.Done)
                        )
                    ),
                    StorybookTheme(
                        id = 3,
                        title = "함께 시간을 보내고 싶을 때",
                        storybooks = listOf(
                            Storybook(31, "취향이 닿는 날", "가족과의 만남"),
                            Storybook(32, "나란히 걷는 날", "가족과의 만남")
                        )
                    ),
                    StorybookTheme(
                        id = 4,
                        title = "마음을 표현하고 싶을 때",
                        storybooks = listOf(
                            Storybook(41, "오늘은 내가 먼저", "가족과의 만남"),
                            Storybook(42, "손을 내미는 연습", "가족과의 만남")
                        )
                    ),
                    StorybookTheme(
                        id = 5,
                        title = "특별한 날을 준비할 때",
                        storybooks = listOf(
                            Storybook(51, "생신까지 열 장", "가족과의 만남"),
                            Storybook(52, "당신의 1호 팬", "가족과의 만남")
                        )
                    )
                ),
                // 진행중 탭 더미 (시작일 빠른 순으로 앞에서부터 좌상단 배치)
                inProgressStorybooks = listOf(
                    InProgressStorybook(101, "오래전 당신", "가족과의 만남", currentChapter = 4, isWaiting = false),
                    InProgressStorybook(102, "오래전 당신", "가족과의 만남", currentChapter = 4, isWaiting = false),
                    InProgressStorybook(103, "오래전 당신", "가족과의 만남", currentChapter = 4, isWaiting = false),
                    InProgressStorybook(104, "오래전 당신", "가족과의 만남", currentChapter = 10, isWaiting = true)
                ),
                // 완료 탭 더미 (10장까지 완료한 시점이 빠른 순으로 앞에서부터 좌상단 배치)
                doneStorybooks = listOf(
                    Storybook(201, "오래전 당신", "가족과의 만남"),
                    Storybook(202, "오래전 당신", "가족과의 만남")
                )
            )
        }
    }
}
