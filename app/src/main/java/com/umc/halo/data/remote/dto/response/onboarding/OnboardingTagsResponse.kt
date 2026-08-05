package com.umc.halo.data.remote.dto.response.onboarding

data class OnboardingTagsResponse(
    val parentPersonalityTags: List<OnboardingTagResponse>,
    val currentRelationStateTags: List<OnboardingTagResponse>,
    val goalRelationshipTags: List<OnboardingTagResponse>
)

data class OnboardingTagResponse(
    val tagId: Long,
    val title: String,
    val subtitle: String?,
    val description: String?
)
