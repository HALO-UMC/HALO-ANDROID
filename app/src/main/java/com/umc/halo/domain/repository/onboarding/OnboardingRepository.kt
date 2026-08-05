package com.umc.halo.domain.repository.onboarding

import com.umc.halo.domain.model.onboarding.OnboardingSaveResult
import com.umc.halo.domain.model.onboarding.OnboardingStatus
import com.umc.halo.domain.model.onboarding.OnboardingTags

interface OnboardingRepository {
    suspend fun isOnboardingCompleted(): Boolean
    suspend fun getStatus(): OnboardingStatus
    suspend fun getTags(): OnboardingTags
    suspend fun checkNickname(name: String): Boolean
    suspend fun saveStep1(name: String): OnboardingSaveResult
    suspend fun saveStep2(gender: String, birthDate: String): OnboardingSaveResult
    suspend fun saveStep3(parentPersonalityTagIds: List<Long>): OnboardingSaveResult
    suspend fun saveStep4(currentRelationStateTagId: Long): OnboardingSaveResult
    suspend fun saveStep5(goalRelationshipTagIds: List<Long>): OnboardingSaveResult
}
