package com.umc.halo.presentation.home

import com.umc.halo.domain.model.home.Books
import com.umc.halo.domain.model.home.CustomizedStoryBooks
import com.umc.halo.domain.model.home.UserInfo
import com.umc.halo.domain.model.home.UserState

data class HomeUiState (
    val userInfo: UserInfo = UserInfo("void",false),
    val userState: UserState = UserState.FTU,
    val greetingMessage: String = "void",
    val bookList: List<Books> = emptyList(),
    val customizedStoryBookList: List<CustomizedStoryBooks> = emptyList()
)
