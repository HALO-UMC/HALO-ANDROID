package com.umc.halo.presentation.home

import androidx.compose.ui.graphics.Color
import com.umc.halo.domain.model.home.Books
import com.umc.halo.domain.model.home.CurrentProgress
import com.umc.halo.domain.model.home.CustomizedStoryBooks
import com.umc.halo.domain.model.home.ProgressState
import com.umc.halo.domain.model.home.Size
import com.umc.halo.domain.model.home.UserInfo
import com.umc.halo.domain.model.home.UserState
import com.umc.halo.presentation.base.BaseViewModel
import com.umc.halo.presentation.base.UiState

class HomeViewModel: BaseViewModel<HomeUiState, HomeUiEvent>(HomeUiState()) {

    override fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnBookClicked -> {
                //스토리북 목차로 navigation
            }

            is HomeUiEvent.OnCustomizedStoryBookClicked -> {
                //스토리북 목차로 navigation
            }

            is HomeUiEvent.OnContinueStoryBookClicked -> {
                //스토리북 상세로 navigation
            }
        }
    }

    init {
        updateState {
            //dummyData
            copy(
                userInfo = UserInfo("김재환", true),
                userState = UserState.RU(
                    currentProgress = CurrentProgress(
                        theme = 1,
                        chapter = 1
                    ),
                    progressState = ProgressState.InProgress
                ),
                greetingMessage = "부모님과의 하루를 기록해보세요.",
                bookList = listOf(
                    Books(1, Color(0xFFD5B689), "오래전 당신", Size(28,84)),
                    Books(2, Color(0xFFFECC3C), "오래전 당신", Size(31,114)),
                    Books(3, Color(0xFFD5B685), "오래전 당신", Size(37,77)),
                    Books(4, Color(0xFFBFBEBE), "오래전 당신", Size(25,71)),
                    Books(5, Color(0xFFFDC04A), "오래전 당신", Size(24,110)),
                    Books(6, Color(0xFFFC9B7F), "오래전 당신", Size(37,90)),
                    Books(7, Color(0xFFB2D965), "오래전 당신", Size(28,71)),
                    Books(8, Color(0xFFFAAABE), "오래전 당신", Size(28,118)),
                    Books(9, Color(0xFF47AA57), "오래전 당신", Size(30,90)),
                    Books(10, Color(0xFFF29EB1), "오래전 당신", Size(24,105))
                ),
                customizedStoryBookList = listOf(
                    CustomizedStoryBooks(1, "대화가 어색한 당신을 위한", "오래전 당신", "가족과의 만남"),
                    CustomizedStoryBooks(2,"대화가 어색한 당신을 위한","오래전 당신","가족과의 만남"),
                    CustomizedStoryBooks(3,"대화가 어색한 당신을 위한","오래전 당신","가족과의 만남"),
                    CustomizedStoryBooks(4,"대화가 어색한 당신을 위한","오래전 당신","가족과의 만남")
                )
            )
        }
    }
}