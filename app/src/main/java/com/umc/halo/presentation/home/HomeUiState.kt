package com.umc.halo.presentation.home

import com.umc.halo.domain.model.home.BookResult
import com.umc.halo.domain.model.home.Books
import com.umc.halo.domain.model.home.StartStorybook
import com.umc.halo.domain.model.home.UserInfo
import com.umc.halo.domain.model.home.UserState
import com.umc.halo.domain.model.storybook.CustomStorybook
import com.umc.halo.domain.model.themebox.ContinueStorybook

data class HomeUiState (
    val userInfo: UserInfo = UserInfo("void",false),
    val userState: UserState = UserState.FTU,
    val bookList: List<Books> = emptyList(),
    val bookShelf: List<BookResult> = emptyList(),
    val customStorybookList: List<CustomStorybook> = emptyList(),
    val continueStorybookList: List<ContinueStorybook> = emptyList(),
    val startStorybook: StartStorybook? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
