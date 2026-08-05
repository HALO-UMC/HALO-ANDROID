package com.umc.halo.data.remote.dto.response.home

data class HomeResponse(
    val homeStatus: String,
    val memberName: String,
    val inProgressStorybooks: List<InProgressStorybookResponse>,
    val bookshelf: List<BookshelfResponse>,
    val recommendedStorybooks: List<RecommendedStorybookResponse>
)

data class InProgressStorybookResponse(
    val storybookId: Long,
    val title: String,
    val currentChapterOrder: Int,
    val totalChapterCount: Int,
    val todayAvailable: Boolean
)

data class BookshelfResponse(
    val storybookId: Long,
    val title: String,
    val shortDescription: String,
    val imageUrl: String,
    val currentChapterOrder: Int?,
    val todayAvailable: Boolean,
    val recommendationReasonText: String?
)

data class RecommendedStorybookResponse(
    val storybookId: Long,
    val title: String,
    val shortDescription: String,
    val imageUrl: String,
    val recommendationReasonText: String
)