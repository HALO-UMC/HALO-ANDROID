package com.umc.halo.domain.model.storybook

/**
 * 챕터의 현재 진행 상태
 */
enum class ChapterStatus {
    LOCKED,
    AVAILABLE,
    COMPLETED
}

/**
 * 스토리북 안에 포함되는 하나의 챕터 정보
 */
data class Chapter(
    val id: Long,
    val storybookId: Long,
    val storybookTitle: String,
    val number: Int,
    val title: String,
    val description: String,

    // 첫 번째 챕터 소개 화면 이미지
    val backgroundImageUrl: String?,

    // 두 번째 시작 안내 화면의 배경 + 캐릭터 통합 이미지
    val guideImageUrl: String?,

    // 스토리북 테마별 장면 선택 모달 캐릭터 이미지
    val characterImageUrl: String?,

    // 스토리북 테마별로 달라지는 상단 공감 문구
    val themeGuideText: String,

    // 각 챕터별로 달라지는 말풍선 문구
    val chapterGuideText: String,

    // 해당 챕터에서 사용자에게 보여줄 질문 목록
    val questions: List<ChapterQuestion>,

    val status: ChapterStatus
)

data class ChapterQuestion(
    val id: Long,
    val order: Int,
    val question: String
)
