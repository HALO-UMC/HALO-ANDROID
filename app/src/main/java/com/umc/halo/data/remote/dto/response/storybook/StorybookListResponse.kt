package com.umc.halo.data.remote.dto.response.storybook

/**
 * GET /api/v1/storybooks 응답 (스토리북 목록 조회)
 */
data class StorybookListResponse(
    val storybooks: List<StorybookSummaryResponse>?,
    val situationalRecommendations: List<SituationalRecommendationResponse>?
)

/**
 * 스토리북 한 권
 *
 * @param status NOT_STARTED / IN_PROGRESS / TODAY_DONE / COMPLETED
 * @param lastChapterOrder 완료한 장 순서. 시작 전이면 null
 * @param lastCompletedDate 마지막 기록일("yyyy-MM-dd"). 시작 전이면 null
 */
data class StorybookSummaryResponse(
    val storybookId: Long,
    val title: String?,
    val themeOrder: Int?,
    val shortDescription: String?,
    val imageUrl: String?,
    val status: String?,
    val lastChapterOrder: Int?,
    val lastCompletedDate: String?
)

/**
 * 상황별 추천 한 카테고리
 *
 * @param tag 섹션 제목으로 쓰는 상황 태그명
 */
data class SituationalRecommendationResponse(
    val tag: String?,
    val storybooks: List<SituationalStorybookResponse>?
)

/**
 * 테마 섹션 안의 스토리북 (카테고리당 2권)
 *
 * 부제/진행 상태는 [StorybookListResponse.storybooks] 쪽에만 있어서 Impl 에서 storybookId 로 이어 붙임
 */
data class SituationalStorybookResponse(
    val storybookId: Long,
    val title: String?,
    val imageUrl: String?,
    val recommendationReasonText: String?
)
