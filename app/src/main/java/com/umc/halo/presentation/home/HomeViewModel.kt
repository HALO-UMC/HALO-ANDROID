package com.umc.halo.presentation.home

import androidx.compose.ui.graphics.Color
import com.umc.halo.presentation.base.BaseViewModel

class HomeViewModel: BaseViewModel<HomeUiState, HomeEvent>(HomeUiState()) {

    override fun onEvent(event: HomeEvent) {
        TODO("Not yet implemented")
    }

    init {
        updateState {
            copy(
                name = "김재환",
                currentProgress = 3,
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
                )
            )
        }
    }
}

data class HomeUiState (
    val name: String = "Void",
    val currentProgress: Int = 0,
    val bookList: List<Books> = emptyList()
)

sealed class HomeEvent (

)

data class Books(
    val id: Int,
    val color: Color,
    val title: String
)