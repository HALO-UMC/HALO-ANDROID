package com.umc.halo.data.remote.dto.request.onboarding

data class OnboardingSaveRequest(
    val step: Int,
    val name: String? = null,
    val gender: String? = null,
    val birthDate: String? = null,
    val parentPersonalityTagIds: List<Long>? = null,
    val currentRelationStateTagId: Long? = null,
    val goalRelationshipTagIds: List<Long>? = null
)
