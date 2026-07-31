package com.umc.halo.domain.model.themebox

data class ContinueStorybook(
    val title: String, //이어하는 스토리북 제목
    val storybookId: Long, // 스토리북 id
    val currentChapterOrder: Long // 앞으로 진행할 스토리북 챕터
)

data class Theme(
    val character: String,
    val title: String,
    val subTitle: String
)