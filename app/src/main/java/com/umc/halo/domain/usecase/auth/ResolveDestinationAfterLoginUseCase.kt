package com.umc.halo.domain.usecase.auth

import com.umc.halo.domain.model.auth.AuthDestination
import com.umc.halo.domain.model.auth.LoginResult
import com.umc.halo.domain.repository.terms.TermsRepository
import javax.inject.Inject

/**
 * 소셜 로그인 성공 직후 어느 화면으로 갈지 판정
 *
 * termsRepository 는 폴백 전용으로 남겨둠
 * 서버 배포가 앱보다 늦어 termsAgreed 가 안 오는 경우(null)에만 예전처럼 조회
 * 실서버에서 값이 항상 온다고 확인되면 이 폴백과 의존성을 지워도 됨
 */
class ResolveDestinationAfterLoginUseCase @Inject constructor(
    private val termsRepository: TermsRepository
) {
    suspend operator fun invoke(loginResult: LoginResult): AuthDestination {
        // 약관 미동의면 무조건 약관부터
        val termsAgreed = loginResult.termsAgreed ?: termsRepository.isTermsAgreed()
        if (!termsAgreed) return AuthDestination.TERMS

        return if (loginResult.onboardingCompleted) {
            AuthDestination.HOME
        } else {
            AuthDestination.ONBOARDING
        }
    }
}
