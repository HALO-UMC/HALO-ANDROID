package com.umc.halo.data.remote.dto.response.themebox

data class ThemeBoxResponse(
    val stats: ThemeBoxStatsResponse,
    val storybooks: List<CompletedStorybookResponse>,
    val currentStorybookId: Long?,
    val inProgressStorybooks: List<InProgressStorybookResponse>,
    val recommendedStorybooks: List<RecommendedStorybookResponse>
)

data class ThemeBoxStatsResponse(
    val collectedCharacterCount: Int,
    val inProgressStorybookCount: Int
)

data class CompletedStorybookResponse(
    val storybookId: Long,
    val order: Int,
    val title: String,
    val summary: String,
    val completedDate: String,
    val characterId: Long,
    val characterName: String,
    val characterImageUrl: String
)

data class InProgressStorybookResponse(
    val storybookId: Long,
    val title: String,
    val nextChapterOrder: Int,
    val todayAvailable: Boolean
)

data class RecommendedStorybookResponse(
    val storybookId: Long,
    val title: String,
    val shortDescription: String,
    val imageUrl: String,
    val recommendationReasonText: String
)