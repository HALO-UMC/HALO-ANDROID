package com.umc.halo.domain.model.storybook

/**
 * 챕터의 현재 진행 상태
 *
 * LOCKED:
 * 이전 챕터를 완료하지 않았거나 다음 날이 되지 않아 접근할 수 없는 상태
 *
 * AVAILABLE:
 * 현재 작성할 수 있는 상태
 *
 * COMPLETED:
 * 이미 작성이 완료되어 결과 조회 화면으로 이동해야 하는 상태
 */
enum class ChapterStatus {
    LOCKED,
    AVAILABLE,
    COMPLETED
}

/**
 * 스토리북 안에 포함되는 하나의 챕터 정보
 *
 * 현재는 서버 API 연결 전이므로 DummyData로 사용하고,
 * 추후 API 응답 모델을 도메인 모델로 변환해 사용할 예정입니다.
 */
data class Chapter(
    val id: Long,
    val storybookId: Long,
    val storybookTitle: String,
    val number: Int,
    val title: String,
    val description: String,
    val backgroundImageUrl: String?,
    val status: ChapterStatus
)