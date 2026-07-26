package com.umc.halo.presentation.storybook.detail

import com.umc.halo.domain.model.home.CurrentProgress
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
                            "지금의 부모님을 이해하고, 더 가까워지는 시간을 선물해보세요.",
                    isCompleted = false
                ),
                storyBookProgress = CurrentProgress(1,8),
                storyBookIndex = listOf(
                    StoryBookIndex(1,"나와 같은 나이였던 시절","부모님이 지금의 내 나이였을 때의 하루",false,true),
                    StoryBookIndex(2,"나와 같은 나이였던 시절","부모님이 지금의 내 나이였을 때의 하루",false,false),
                    StoryBookIndex(3,"나와 같은 나이였던 시절","부모님이 지금의 내 나이였을 때의 하루",true,false)
                )
            )
        }
    }
}