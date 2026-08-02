package com.umc.halo.domain.model.home

import androidx.compose.ui.graphics.Color

/**
 * 홈 화면 책장에서 쓰이는 책 정보
 */
data class Books(
    val id: Long,
    val title: String, // 제목
    val subtitle: String, // 부제목
    val spineImage: Int, // 책 등 이미지 ( 현재는 프론트엔드 @drawable 이미지를 사용합니다 )
    val coverImage: Int, // 책 표지 이미지  ( 현재는 프론트엔드 @drawable 이미지를 사용합니다 )
    val height: Int, // 책 높이
    val width: Int, // 책 너비
    val tilt: Float, // 책 기울기
    val offsetY: Float, // 기울기에 따른 y축 이동
    val offsetX: Int, // 기울기에 따른 x축 너비 변경
    val currentProgress: Int, // 현재 진행 상황
    val isCompleted: Boolean // 오늘자 스토리북 완료 여부
)

/**
 * 책장 클릭 시 띄워지는 '스토리북으로 이동하기' 정보
 */
data class StartStorybook(
    val storybookId: Long = 0,
    val title: String = "void",
    val tag: String = "void", // 상단 태그
    val currentProgress: Int = 0,
    val isCompleted: Boolean = false,
    val isFirst: Boolean = false
)