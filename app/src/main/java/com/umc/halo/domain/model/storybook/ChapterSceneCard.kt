package com.umc.halo.domain.model.storybook

/**
 * 사진 업로드 대신 선택할 수 있는 장면카드
 *
 * 나중에는 백엔드에서 id, imageUrl 등을 내려받아 사용합니다.
 */
data class ChapterSceneCard(
    val id: Long,
    val storybookId: Long,
    val chapterId: Long,
    val title: String,
    val imageUrl: String?
)