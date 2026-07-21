package com.umc.halo.presentation.themebox

import com.umc.halo.domain.model.storybook.CustomStorybook
import com.umc.halo.presentation.base.BaseViewModel

class ThemeBoxViewModel: BaseViewModel<ThemeBoxUiState, ThemeBoxUiEvent>(ThemeBoxUiState.Empty.FTU()) {
    override fun onEvent(event: ThemeBoxUiEvent) {
        TODO("Not yet implemented")
    }

    init {
        updateState {
//            ThemeBoxUiState.Filled(
//                numberOfCharacter = 4,
//                storyBookInProgress = 3,
//                themeList = listOf(
//                    Theme("할로로","오래전 당신","가족과의 만남"),
//                    Theme("케로로","당신 사용 설명서", "부제"),
//                    Theme("기로로","가족의 온도", "부제"),
//                    Theme("도로로","취향이 닿는 날", "부제")
//                )
//            )
            ThemeBoxUiState.Empty.FTU(
                customStorybookList = listOf(
                    CustomStorybook(1, "대화가 어색한 당신을 위한", "오래전 당신", "가족과의 만남"),
                    CustomStorybook(2,"대화가 어색한 당신을 위한","오래전 당신","가족과의 만남"),
                    CustomStorybook(3,"대화가 어색한 당신을 위한","오래전 당신","가족과의 만남"),
                    CustomStorybook(4,"대화가 어색한 당신을 위한","오래전 당신","가족과의 만남")
                )
            )
            ThemeBoxUiState.Empty.RU(

            )
        }
    }
}