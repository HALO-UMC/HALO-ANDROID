package com.umc.halo.domain.model.themebox

data class ContinueStorybook(
    val title: String, //이어하는 스토리북 제목
    val theme: Int, // 스토리북 id
    val chapter: Int // 챕터 id
)

data class Theme(
    val character: String,
    val title: String,
    val subTitle: String
)