package com.umc.halo.domain.model.onboarding

data class OnboardingStatus(
    val onboardingCompleted: Boolean,
    val currentStep: Int?,
    val savedData: OnboardingSavedData?
)

data class OnboardingSavedData(
    val name: String?,
    val gender: String?,
    val birthDate: String?,
    val parentPersonalityTagIds: List<Long>,
    val currentRelationStateTagId: Long?,
    val goalRelationshipTagIds: List<Long>
)
