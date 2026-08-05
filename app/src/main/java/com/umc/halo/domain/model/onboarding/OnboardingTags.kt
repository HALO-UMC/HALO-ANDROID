package com.umc.halo.domain.model.onboarding

data class OnboardingTags(
    val parentPersonalityTags: List<OnboardingTag>,
    val currentRelationStateTags: List<OnboardingTag>,
    val goalRelationshipTags: List<OnboardingTag>
)
