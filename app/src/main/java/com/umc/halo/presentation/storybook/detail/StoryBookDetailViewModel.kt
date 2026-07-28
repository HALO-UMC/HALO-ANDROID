package com.umc.halo.presentation.storybook.detail

import com.umc.halo.domain.model.storybook.StoryBookIndex
import com.umc.halo.domain.model.storybook.StoryBookInfo
import com.umc.halo.domain.model.storybook.StorybookProgress
import com.umc.halo.domain.model.storybook.TodayStoryBook
import com.umc.halo.presentation.base.BaseViewModel
import kotlin.collections.listOf

class StoryBookDetailViewModel: BaseViewModel<StoryBookDetailUiState, StoryBookDetailUiEvent>(
    StoryBookDetailUiState()
) {
    override fun onEvent(event: StoryBookDetailUiEvent) {
        when (event) {
            is StoryBookDetailUiEvent.OnClickDismissDialog -> {
                updateState {
                    copy(
                        showDialog = false
                    )
                }
            }

            is StoryBookDetailUiEvent.OnClickOpenDialog -> {
                updateState {
                    copy(
                        showDialog = true
                    )
                }
            }

            else -> Unit
        }
    }

    init {
        updateState {
            //dummyData
            copy(
                storyBookId = 1,
                storyBookInfo = StoryBookInfo(
                    title = "오래전 당신",
                    storyBookIntro = "부모님을 ‘부모'가 아닌 한 사람으로 바라보며, 어린 시절의 기억과 \n" +
                            "청춘의 순간, 지나온 시간을 차근차근 들어보는 이야기입니다. \n" +
                            "지금의 부모님을 이해하고, 더 가까워지는 시간을 선물해보세요."
                ),
                storyBookProgress = StorybookProgress.Done,
                storyBookIndex = listOf(
                    StoryBookIndex(1, "나와 같은 나이였던 시절", "부모님이 지금의 내 나이였을 때의 하루", false, false),
                    StoryBookIndex(2,"소년과 소녀의 꿈","어린 시절에 품었던 작은 꿈들",true,false),
                    StoryBookIndex(3,"빛나던 청춘의 한 페이지","설렘과 도전으로 가득했던 날들",true,false),
                    StoryBookIndex(4,"첫 출근과 첫 월급","설렘과 도전으로 가득했던 날들",true,false),
                    StoryBookIndex(5,"두 사람의 서막","우리의 시작을 이야기해요",true,false),
                    StoryBookIndex(6,"가장 용기냈던 순간","지금의 내 나이였을 때의 하루",true,false),
                    StoryBookIndex(7,"잊지 못할 사람들","지금의 내 나이였을 때의 하루",true,false),
                    StoryBookIndex(8,"부모가 되기 전의밤","지금의 내 나이였을 때의 하루",true,false),
                    StoryBookIndex(9,"돌아오는 길","지금의 내 나이였을 때의 하루",true,false),
                    StoryBookIndex(10,"다시 쓰는 당신의 프로필","지금의 내 나이였을 때의 하루",true,false)
                ),
                todayStoryBookInfo = TodayStoryBook(
                    1,
                    "나와 같은 나이였던 시절",
                    "부모님을 한 사람으로 바라보는 첫 장입니다.\n지금의 내 나이였을 때 부모님은 어떤 하루를 살고 있었는지 돌아봅니다.",
                    false,
                    false
                )
            )
        }
    }
}