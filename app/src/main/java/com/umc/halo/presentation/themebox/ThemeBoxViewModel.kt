package com.umc.halo.presentation.themebox

import com.umc.halo.domain.model.storybook.CustomStorybook
import com.umc.halo.domain.model.themebox.Theme
import com.umc.halo.presentation.base.BaseViewModel

class ThemeBoxViewModel: BaseViewModel<ThemeBoxUiState, ThemeBoxUiEvent>(ThemeBoxUiState.Empty.FTU()) {
    override fun onEvent(event: ThemeBoxUiEvent) {
        when (event) {
            is ThemeBoxUiEvent.OnPagerChanged -> {
                updateState {
                    ThemeBoxUiState.Filled(currentStorybookId = event.page.toLong())
                }
            }

            else -> Unit
        }
    }

    init {
        updateState {
            ThemeBoxUiState.Filled(
                numberOfCharacter = 4,
                storyBookInProgress = 3,
                themeList = listOf(
                    Theme("테마 1", "오래전 당신", "가족과의 만남"),
                    Theme("테마 2","당신 사용 설명서", "부제"),
                    Theme("테마 3","가족의 온도", "부제"),
                    Theme("테마 4","취향이 닿는 날", "부제")
                )
            )
//            ThemeBoxUiState.Empty.FTU(
//                customStorybookList = listOf(
//                    CustomStorybook(1, "대화가 어색한 당신을 위한", "오래전 당신", "가족과의 만남"),
//                    CustomStorybook(2,"대화가 어색한 당신을 위한","오래전 당신","가족과의 만남"),
//                    CustomStorybook(3,"대화가 어색한 당신을 위한","오래전 당신","가족과의 만남"),
//                    CustomStorybook(4,"대화가 어색한 당신을 위한","오래전 당신","가족과의 만남")
//                )
//            )
//            ThemeBoxUiState.Empty.RU(
//
//            )
        }
    }
}