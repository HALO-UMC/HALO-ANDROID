package com.umc.halo.data.remote.dto.response.storybook

/**
 * GET /api/v1/storybooks/recommended 응답 (추천 스토리북 조회)
 */
data class RecommendedStorybookListResponse(
    val storybooks: List<RecommendedStorybookResponse>?
)

/**
 * @param recommendationReasonText 추천 사유 문구. 카드 위쪽 주황색 태그로 표시
 */
data class RecommendedStorybookResponse(
    val storybookId: Long,
    val title: String?,
    val shortDescription: String?,
    val imageUrl: String?,
    val recommendationReasonText: String?
)
