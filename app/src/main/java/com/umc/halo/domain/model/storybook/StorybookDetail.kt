package com.umc.halo.domain.model.storybook

/**
 * 스토리북 목차에서 사용하는 스토리북 정보
 */
data class StoryBookInfo(
    val title: String,
    val storyBookIntro: String, // 소개 글
    val imageUrl: String // 표지 사진 Url
)

/**
 * 스토리북 목차 하단 리스트 정보
 */
data class StoryBookIndex(
    val id: Long,
    val title: String,
    val subTitle: String,
    val description: String,
    val imageUrl: String, // 표지 사진 Url
    val isLocked: Boolean, // 잠김 여부 (아직 열리지 않은 스토리북 : 클릭 시 모달 생성)
    val isCompleted: Boolean // 완료 여부 (스토리북 전체가 완료 되었을 경우: 클릭 시 chapter result로 이동)
)

/**
 * 오늘의 스토리북 정보
 */
data class TodayStoryBook(
    val id: Long,
    val title: String,
    val imageUrl: String, // 표지 사진 Url
    val tag: String, // 소개 글
    val isLocked: Boolean, // 잠김 여부 (오늘의 스토리북이 완료 되었을 경우: 잠긴 스토리북을 오늘의 스토리북에 표시)
    val isCompleted: Boolean // 완료 여부 (스토리북 전체가 완료 되었을 경우: 전체 스토리북 감상을 오늘의 스토리북에 표시)
)

/**
 * getStorybookDetail 실행 시 response
 */
data class StorybookDetailResult(
    val storyBookId: Long,
    val storyBookInfo: StoryBookInfo,
    val storyBookProgress: StorybookProgress,
    val storyBookIndex: List<StoryBookIndex>,
)

enum class StorybookIndexStatus {
    COMPLETED,
    TODAY,
    TODAY_LOCKED,
    LOCKED;

    companion object {
        fun from(value: String): StorybookIndexStatus =
            entries.find { it.name == value } ?: LOCKED
    }
}