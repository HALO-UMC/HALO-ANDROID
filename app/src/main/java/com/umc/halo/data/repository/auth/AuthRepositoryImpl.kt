package com.umc.halo.data.repository.auth

import com.umc.halo.core.datastore.TokenDataStore
import com.umc.halo.data.remote.api.auth.AuthApi
import com.umc.halo.data.remote.dto.request.auth.LoginRequest
import com.umc.halo.data.remote.dto.request.auth.ReissueRequest
import com.umc.halo.domain.model.auth.AuthSession
import com.umc.halo.domain.model.auth.LoginResult
import com.umc.halo.domain.model.auth.SocialProvider
import com.umc.halo.domain.repository.auth.AuthRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * AuthRepository 구현체
 * 서버 호출(AuthApi) → 성공 시 토큰 저장(TokenDataStore) → DTO 를 도메인 모델로 변환
 */
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenDataStore: TokenDataStore
) : AuthRepository {

    override suspend fun login(provider: SocialProvider, providerToken: String): LoginResult {
        val response = authApi.login(
            LoginRequest(
                provider = provider.name,   // enum → KAKAO / GOOGLE
                providerToken = providerToken
            )
        )

        // 공통 래퍼에서 result 추출 (실패/빈 결과면 예외 → ViewModel 의 runCatching 이 처리)
        val result = response.result
        if (!response.isSuccess || result == null) {
            error("로그인 실패 (code=${response.code}, message=${response.message})")
        }

        // 서버 토큰 저장 (자동 로그인/인증 헤더에서 재사용)
        tokenDataStore.saveTokens(result.accessToken, result.refreshToken)

        // DTO → 도메인 변환
        // isNewUser 는 화면 분기에 쓰지 않음
        return LoginResult(
            isNewUser = result.isNewUser,
            onboardingCompleted = result.onboardingCompleted,
            termsAgreed = result.termsAgreed
        )
    }

    override suspend fun reissue(): AuthSession? {
        val refreshToken = tokenDataStore.refreshTokenFlow.first()
        if (refreshToken.isNullOrBlank()) return null

        // 만료(AUTH401_2)·네트워크 오류 모두 '자동 로그인 실패'로 같게 처리
        val result = runCatching { authApi.reissue(ReissueRequest(refreshToken)) }
            .getOrNull()
            ?.takeIf { it.isSuccess }
            ?.result

        if (result == null) {
            tokenDataStore.clear()
            return null
        }

        tokenDataStore.saveTokens(result.accessToken, result.refreshToken)

        return AuthSession(
            termsAgreed = result.termsAgreed,
            onboardingCompleted = result.onboardingCompleted
        )
    }

    override suspend fun logout() {
        // 서버 무효화는 실패해도 무시(토큰 만료 등)
        runCatching { authApi.logout() }
        tokenDataStore.clear()
    }
}
