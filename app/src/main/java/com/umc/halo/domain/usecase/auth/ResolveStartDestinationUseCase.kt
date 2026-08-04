package com.umc.halo.domain.usecase.auth

import com.umc.halo.domain.model.auth.AuthDestination
import com.umc.halo.domain.repository.auth.AuthRepository
import com.umc.halo.domain.repository.onboarding.OnboardingRepository
import com.umc.halo.domain.repository.terms.TermsRepository
import javax.inject.Inject

/**
 * 앱 실행(스플래시) 시 어느 화면으로 들어갈지 판정
 *
 * 저장된 refreshToken 으로 자동 로그인을 시도한 뒤
 * 약관 → 온보딩 순서로 서버 상태를 확인해 첫 화면을 결정
 *
 * termsRepository·onboardingRepository 는 폴백 전용으로 남겨둠
 * 서버 배포가 앱보다 늦어 두 필드가 안 오는 경우(null)에만 예전처럼 각각 조회
 * 실서버에서 두 값이 항상 온다고 확인되면 이 폴백과 의존성 2개를 지워도 됨
 */
class ResolveStartDestinationUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val termsRepository: TermsRepository,
    private val onboardingRepository: OnboardingRepository
) {
    suspend operator fun invoke(): AuthDestination {
        // 1순위 : 로그인 여부 (재발급 실패 = 토큰 없음/만료 → 로컬 토큰도 정리된 상태)
        val session = authRepository.reissue() ?: return AuthDestination.LOGIN

        // 2순위 : 필수 약관 동의 여부
        val termsAgreed = session.termsAgreed ?: termsRepository.isTermsAgreed()
        if (!termsAgreed) return AuthDestination.TERMS

        // 3순위 : 온보딩 완료 여부
        val onboardingCompleted =
            session.onboardingCompleted ?: onboardingRepository.isOnboardingCompleted()
        if (!onboardingCompleted) return AuthDestination.ONBOARDING

        return AuthDestination.HOME
    }
}
