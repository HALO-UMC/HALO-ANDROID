package com.umc.halo.domain.model.themebox

import com.umc.halo.domain.model.storybook.CustomStorybook

data class ContinueStorybook(
    val title: String, //이어하는 스토리북 제목
    val storybookId: Long, // 스토리북 id
    val currentChapterOrder: Long, // 앞으로 진행할 스토리북 챕터
    val todayAvailable: Boolean // 오늘 가능 여부
)

data class Theme(
    val storybookId: Long, // 이 테마에 해당하는 스토리북 id
    val character: String,
    val title: String,
    val subTitle: String,
    val imageUrl: String
)

data class ThemeBoxResult(
    val numberOfCharacter: Int,
    val storyBookInProgress: Int,
    val themeList: List<Theme>,
    val currentStorybookId: Long?,
    val continueStorybookList: List<ContinueStorybook>,
    val customStorybookList: List<CustomStorybook>
)