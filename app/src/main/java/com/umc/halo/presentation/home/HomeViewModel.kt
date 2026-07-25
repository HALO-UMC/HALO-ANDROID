package com.umc.halo.presentation.home

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.umc.halo.R
import com.umc.halo.domain.model.home.Books
import com.umc.halo.domain.model.home.CurrentProgress
import com.umc.halo.domain.model.home.ProgressState
import com.umc.halo.domain.model.home.StartStorybook
import com.umc.halo.domain.model.home.UserInfo
import com.umc.halo.domain.model.home.UserState
import com.umc.halo.domain.model.storybook.CustomStorybook
import com.umc.halo.domain.model.themebox.ContinueStorybook
import com.umc.halo.presentation.base.BaseViewModel
import com.umc.halo.presentation.base.UiState
import com.umc.halo.presentation.themebox.ContinueStorybook

class HomeViewModel: BaseViewModel<HomeUiState, HomeUiEvent>(HomeUiState()) {

    override fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnBookClicked -> {
                selectedStorybook(event.storyBookId)
            }

            is HomeUiEvent.OnCustomizedStoryBookClicked -> {
                //스토리북 목차로 navigation
            }

            is HomeUiEvent.OnContinueStoryBookClicked -> {
                //스토리북 상세로 navigation
            }
        }
    }

    private fun selectedStorybook(id: Int?) {
        val book = _uiState.value.bookList.find { it.id == id }

        Log.d("test",book.toString())

        if (book == null) {
            updateState {
                copy(
                    startStorybook = null
                )
            }
        } else {
            updateState {
                copy(
                    startStorybook = StartStorybook(
                        storybookId = book.id,
                        title = book.title,
                        subtitle = book.subtitle,
                        currentProgress = book.currentProgress,
                        isCompleted = book.isCompleted
                    )
                )
            }
        }
    }

    init {
        updateState {
            //dummyData
            copy(
                userInfo = UserInfo("김재환", true),
                userState = UserState.RU,
                greetingMessage = "부모님과의 하루를 기록해보세요.",
                bookList = listOf(
                    Books(1, "오래전 당신", "가족과의 만남",R.drawable.image_home_bookcase_1, R.drawable.image_home_bookcase_cover_1,240, 48, 0f, 0f, 48, 5, true),
                    Books(2, "당신의 1호 팬", "부제",R.drawable.image_home_bookcase_2, R.drawable.image_home_bookcase_cover_1, 230, 68, -14.02f, -13f, 121, 0, false),
                    Books(3, "취향이 닿는 날", "부제",R.drawable.image_home_bookcase_3, R.drawable.image_home_bookcase_cover_1, 230, 37, 0f, 0f, 37, 0, false),
                    Books(4, "오늘은 내가 먼저", "부제",R.drawable.image_home_bookcase_4, R.drawable.image_home_bookcase_cover_1, 230, 61, 3.61f, 3.6f, 75, 0, false),
                    Books(5, "생신까지 열 장", "부제",R.drawable.image_home_bookcase_5,  R.drawable.image_home_bookcase_cover_1, 230, 52, 0f, 0f, 52, 0, false),
                    Books(6, "나란히 걷는 날", "부제",R.drawable.image_home_bookcase_6, R.drawable.image_home_bookcase_cover_1, 230, 46, -6.2f, -0.7f, 71, 0, false),
                    Books(7, "당신 사용 설명서", "부제",R.drawable.image_home_bookcase_7,R.drawable.image_home_bookcase_cover_1, 230, 76, 0f, 0f, 76,0, false),
                    Books(8, "한 장의 가족 사진", "부제",R.drawable.image_home_bookcase_8, R.drawable.image_home_bookcase_cover_1, 230, 38, 0f, 0f, 38,0, false),
                    Books(9, "손을 내미는 연습", "부제", R.drawable.image_home_bookcase_9, R.drawable.image_home_bookcase_cover_1, 230, 64, 7.65f,7.5f, 95,0, false),
                    Books(10, "가족의 온도", "부제", R.drawable.image_home_bookcase_10, R.drawable.image_home_bookcase_cover_1, 230, 50,0f,0f, 50,0, false)
                ),
                customStorybookList = listOf(
                    CustomStorybook(1, "대화가 어색한 당신을 위한", "오래전 당신", "가족과의 만남"),
                    CustomStorybook(2,"대화가 어색한 당신을 위한","오래전 당신","가족과의 만남")
                ),
                continueStorybookList = listOf(
                    ContinueStorybook("오래전 당신",1,5),
                    ContinueStorybook("오래전 당신",1,5),
                    ContinueStorybook("오래전 당신",1,5),
                    ContinueStorybook("오래전 당신",1,5)
                ),
                startStorybook = null
            )
        }
    }
}

