package com.umc.halo.domain.usecase.auth

import com.umc.halo.domain.model.auth.AuthDestination
import com.umc.halo.domain.model.auth.LoginResult
import com.umc.halo.domain.repository.terms.TermsRepository
import javax.inject.Inject

/**
 * 소셜 로그인 성공 직후 어느 화면으로 갈지 판정
 *
 * 스플래시(ResolveStartDestinationUseCase)와 달리 토큰은 방금 발급받았고
 * 온보딩 완료 여부도 로그인 응답에 들어 있으므로 약관 여부만 서버에 확인
 *
 * TODO: 백엔드에 /auth/login 응답으로 termsAgreed 를 함께 달라고 요청해둠
 *  추가되면 이 API 호출 생략 가능
 */
class ResolveDestinationAfterLoginUseCase @Inject constructor(
    private val termsRepository: TermsRepository
) {
    suspend operator fun invoke(loginResult: LoginResult): AuthDestination {
        // 약관 미동의면 무조건 약관부터
        if (!termsRepository.isTermsAgreed()) return AuthDestination.TERMS

        return if (loginResult.onboardingCompleted) {
            AuthDestination.HOME
        } else {
            AuthDestination.ONBOARDING
        }
    }
}
