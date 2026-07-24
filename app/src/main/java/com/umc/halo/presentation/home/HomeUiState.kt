package com.umc.halo.presentation.home

import com.umc.halo.domain.model.home.Books
import com.umc.halo.domain.model.home.UserInfo
import com.umc.halo.domain.model.home.UserState
import com.umc.halo.domain.model.storybook.CustomStorybook
import com.umc.halo.domain.model.themebox.ContinueStorybook

data class HomeUiState (
    val userInfo: UserInfo = UserInfo("void",false),
    val userState: UserState = UserState.FTU,
    val greetingMessage: String = "void",
    val bookList: List<Books> = emptyList(),
    val customStorybookList: List<CustomStorybook> = emptyList(),
    val continueStorybookList: List<ContinueStorybook> = emptyList()
)
