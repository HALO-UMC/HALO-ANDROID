package com.umc.halo.data.repository.onboarding

import com.umc.halo.core.network.BaseResponse
import com.umc.halo.data.remote.api.onboarding.OnboardingApi
import com.umc.halo.data.remote.dto.request.onboarding.OnboardingSaveRequest
import com.umc.halo.data.remote.dto.response.onboarding.OnboardingSaveResponse
import com.umc.halo.data.remote.dto.response.onboarding.OnboardingSavedDataResponse
import com.umc.halo.data.remote.dto.response.onboarding.OnboardingStatusResponse
import com.umc.halo.data.remote.dto.response.onboarding.OnboardingTagResponse
import com.umc.halo.data.remote.dto.response.onboarding.OnboardingTagsResponse
import com.umc.halo.domain.model.onboarding.OnboardingSaveResult
import com.umc.halo.domain.model.onboarding.OnboardingSavedData
import com.umc.halo.domain.model.onboarding.OnboardingStatus
import com.umc.halo.domain.model.onboarding.OnboardingTag
import com.umc.halo.domain.model.onboarding.OnboardingTags
import com.umc.halo.domain.repository.onboarding.OnboardingRepository
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val onboardingApi: OnboardingApi
) : OnboardingRepository {

    override suspend fun isOnboardingCompleted(): Boolean =
        runCatching { getStatus().onboardingCompleted }
            .getOrDefault(false)

    override suspend fun getStatus(): OnboardingStatus {
        val response = onboardingApi.getStatus()
        return response.requireResult("onboarding status").toDomain()
    }

    override suspend fun getTags(): OnboardingTags {
        val response = onboardingApi.getTags()
        return response.requireResult("onboarding tags").toDomain()
    }

    override suspend fun checkNickname(name: String): Boolean {
        val response = onboardingApi.checkNickname(name)
        return response.requireResult("nickname check").isAvailable
    }

    override suspend fun saveStep1(name: String): OnboardingSaveResult =
        save(OnboardingSaveRequest(step = 1, name = name))

    override suspend fun saveStep2(
        gender: String,
        birthDate: String
    ): OnboardingSaveResult =
        save(
            OnboardingSaveRequest(
                step = 2,
                gender = gender,
                birthDate = birthDate
            )
        )

    override suspend fun saveStep3(
        parentPersonalityTagIds: List<Long>
    ): OnboardingSaveResult =
        save(
            OnboardingSaveRequest(
                step = 3,
                parentPersonalityTagIds = parentPersonalityTagIds
            )
        )

    override suspend fun saveStep4(
        currentRelationStateTagId: Long
    ): OnboardingSaveResult =
        save(
            OnboardingSaveRequest(
                step = 4,
                currentRelationStateTagId = currentRelationStateTagId
            )
        )

    override suspend fun saveStep5(
        goalRelationshipTagIds: List<Long>
    ): OnboardingSaveResult =
        save(
            OnboardingSaveRequest(
                step = 5,
                goalRelationshipTagIds = goalRelationshipTagIds
            )
        )

    private suspend fun save(request: OnboardingSaveRequest): OnboardingSaveResult {
        val response = onboardingApi.saveOnboarding(request)
        return response.requireResult("onboarding save").toDomain()
    }

    private fun <T> BaseResponse<T>.requireResult(label: String): T {
        if (!isSuccess || result == null) {
            error("$label failed: code=$code, message=$message")
        }

        return result
    }
}

private fun OnboardingTagsResponse.toDomain() = OnboardingTags(
    parentPersonalityTags = parentPersonalityTags.map { it.toDomain() },
    currentRelationStateTags = currentRelationStateTags.map { it.toDomain() },
    goalRelationshipTags = goalRelationshipTags.map { it.toDomain() }
)

private fun OnboardingTagResponse.toDomain() = OnboardingTag(
    id = tagId,
    title = title,
    subtitle = subtitle,
    description = description
)

private fun OnboardingStatusResponse.toDomain() = OnboardingStatus(
    onboardingCompleted = onboardingCompleted,
    currentStep = currentStep,
    savedData = savedData?.toDomain()
)

private fun OnboardingSavedDataResponse.toDomain() = OnboardingSavedData(
    name = name,
    gender = gender,
    birthDate = birthDate,
    parentPersonalityTagIds = parentPersonalityTagIds,
    currentRelationStateTagId = currentRelationStateTagId,
    goalRelationshipTagIds = goalRelationshipTagIds
)

private fun OnboardingSaveResponse.toDomain() = OnboardingSaveResult(
    onboardingStep = onboardingStep,
    onboardingCompleted = onboardingCompleted
)
