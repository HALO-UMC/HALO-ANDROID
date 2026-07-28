package com.umc.halo.data.repository.onboarding

import com.umc.halo.data.remote.api.onboarding.OnboardingApi
import com.umc.halo.domain.repository.onboarding.OnboardingRepository
import javax.inject.Inject

/**
 * OnboardingRepository 구현체
 */
class OnboardingRepositoryImpl @Inject constructor(
    private val onboardingApi: OnboardingApi
) : OnboardingRepository {

    override suspend fun isOnboardingCompleted(): Boolean =
        runCatching { onboardingApi.getStatus() }
            .getOrNull()
            ?.takeIf { it.isSuccess }
            ?.result
            ?.onboardingCompleted
            ?: false   // 조회 실패 시 '미완료'로 → 온보딩을 다시 보여줌
}
