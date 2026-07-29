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
 *
 * TODO: 백엔드에 /auth/reissue 응답으로 termsAgreed·onboardingCompleted 를 함께 달라고 요청해둠
 *  추가되면 아래 API 호출 3번이 1번으로 줄어듬. 그때 이 파일만 수정예정
 */
class ResolveStartDestinationUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val termsRepository: TermsRepository,
    private val onboardingRepository: OnboardingRepository
) {
    suspend operator fun invoke(): AuthDestination {
        // 1순위 : 로그인 여부 (재발급 실패 = 토큰 없음/만료 → 로컬 토큰도 정리된 상태)
        if (!authRepository.reissue()) return AuthDestination.LOGIN

        // 2순위 : 필수 약관 동의 여부
        if (!termsRepository.isTermsAgreed()) return AuthDestination.TERMS

        // 3순위 : 온보딩 완료 여부
        if (!onboardingRepository.isOnboardingCompleted()) return AuthDestination.ONBOARDING

        return AuthDestination.HOME
    }
}
