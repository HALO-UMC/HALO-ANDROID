package com.umc.halo.presentation.home

import androidx.compose.ui.graphics.Color
import com.umc.halo.presentation.base.BaseViewModel

class HomeViewModel: BaseViewModel<HomeUiState, HomeEvent>(HomeUiState()) {

    override fun onEvent(event: HomeEvent) {
        TODO("Not yet implemented")
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
                    Books(1, Color.Red, "오래전 당신"),
                    Books(2, Color.Blue, "오래전 당신"),
                    Books(3, Color.Gray, "오래전 당신"),
                    Books(4, Color.Cyan, "오래전 당신"),
                    Books(5, Color.Green, "오래전 당신"),
                    Books(6, Color.Yellow, "오래전 당신"),
                    Books(7, Color.Black, "오래전 당신"),
                    Books(8, Color.Cyan, "오래전 당신"),
                    Books(9, Color.Green, "오래전 당신"),
                    Books(10, Color.Yellow, "오래전 당신"),
                    Books(11, Color.Black, "오래전 당신")
                ),
                customizedStoryBookList = listOf(
                    CustomizedStoryBooks(1,"대화가 어색한 당신을 위한","오래전 당신","가족과의 만남"),
                    CustomizedStoryBooks(2,"대화가 어색한 당신을 위한","오래전 당신","가족과의 만남"),
                    CustomizedStoryBooks(3,"대화가 어색한 당신을 위한","오래전 당신","가족과의 만남"),
                    CustomizedStoryBooks(4,"대화가 어색한 당신을 위한","오래전 당신","가족과의 만남")
                )
            )
        }
    }
}

data class HomeUiState (
    val userInfo: UserInfo = UserInfo("void",false),
    val userState: UserState = UserState.FTU,
    val greetingMessage: String = "void",
    val bookList: List<Books> = emptyList(),
    val customizedStoryBookList: List<CustomizedStoryBooks> = emptyList()
)

sealed class HomeEvent (

)

data class Books(
    val id: Int,
    val color: Color,
    val title: String
)

data class CustomizedStoryBooks(
    val id: Int,
    val intro: String,
    val title: String,
    val subtitle: String
)

enum class ProgressState {
    InProgress,
    Complete,
    BeforeStart
}

data class CurrentProgress(
    val theme: Int,
    val chapter: Int
)

data class UserInfo(
    val name: String,
    val auth: Boolean
)

sealed interface UserState {
    data object FTU: UserState
    data class RU(
        val currentProgress: CurrentProgress,
        val progressState: ProgressState
    ): UserState
}