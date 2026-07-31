package com.umc.halo.domain.model.themebox

data class ContinueStorybook(
    val title: String, //이어하는 스토리북 제목
    val storybookId: Long, // 스토리북 id
    val currentProgress: Long
)

data class Theme(
    val storybookId: Long, // 이 테마에 해당하는 스토리북 id
    val character: String,
    val title: String,
    val subTitle: String
)