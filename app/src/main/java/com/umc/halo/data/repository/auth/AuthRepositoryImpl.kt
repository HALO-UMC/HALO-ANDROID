package com.umc.halo.data.repository.auth

import com.umc.halo.core.datastore.LastLoginDataStore
import com.umc.halo.core.datastore.TokenDataStore
import com.umc.halo.core.network.ReissueResult
import com.umc.halo.core.network.TokenRefresher
import com.umc.halo.core.network.toApiErrorMessage
import com.umc.halo.data.remote.api.auth.AuthApi
import com.umc.halo.data.remote.dto.request.auth.LoginRequest
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
    private val tokenRefresher: TokenRefresher,
    private val tokenDataStore: TokenDataStore,
    private val lastLoginDataStore: LastLoginDataStore
) : AuthRepository {

    override suspend fun login(provider: SocialProvider, providerToken: String): LoginResult {
        // 통신 자체가 실패한 경우(네트워크 단절 등)와 서버가 실패로 응답한 경우를 모두
        // 사용자에게 보여줄 문구로 바꿔서 던짐 -> ViewModel 의 runCatching 이 그대로 표시
        val response = runCatching {
            authApi.login(
                LoginRequest(
                    provider = provider.name,   // enum → KAKAO / GOOGLE
                    providerToken = providerToken
                )
            )
        }.getOrElse { throwable ->
            throw IllegalStateException(throwable.toApiErrorMessage(LOGIN_FAILED_MESSAGE))
        }

        // 공통 래퍼에서 result 추출 (실패/빈 결과면 예외 → ViewModel 의 runCatching 이 처리)
        val result = response.result
        if (!response.isSuccess || result == null) {
            error(response.toApiErrorMessage(LOGIN_FAILED_MESSAGE))
        }

        // 서버 토큰 저장 (자동 로그인/인증 헤더에서 재사용)
        tokenDataStore.saveTokens(result.accessToken, result.refreshToken)

        // 재로그인 화면의 '최근 로그인' 표시에 쓸 로그인 방식 기록 (로그아웃해도 지우지 않음)
        lastLoginDataStore.saveProvider(provider.name)

        // DTO → 도메인 변환
        // isNewUser 는 화면 분기에 쓰지 않음
        return LoginResult(
            isNewUser = result.isNewUser,
            onboardingCompleted = result.onboardingCompleted,
            termsAgreed = result.termsAgreed
        )
    }

    /**
     * 스플래시의 자동 로그인
     *
     * 재발급 자체는 [TokenRefresher] 가 전담
     *
     * 실패하면 어느 쪽이든 null. 다만 후처리는 다음과 같음
     * - [ReissueResult.Expired] : 토큰을 지운 상태. 재로그인해야 함
     * - [ReissueResult.Failed]  : 네트워크 문제일 뿐이라 토큰 보존 -> 다음 실행에서 자동 로그인 재시도
     */
    override suspend fun reissue(): AuthSession? {
        val session = when (val result = tokenRefresher.refreshForAutoLogin()) {
            is ReissueResult.Success -> result.session ?: return null
            ReissueResult.Expired, ReissueResult.Failed -> return null
        }

        // DTO -> 도메인 변환
        return AuthSession(
            termsAgreed = session.termsAgreed,
            onboardingCompleted = session.onboardingCompleted
        )
    }

    override suspend fun logout() {
        // 서버 무효화는 실패해도 무시(토큰 만료 등)
        runCatching { authApi.logout() }
        tokenDataStore.clear()
    }

    override suspend fun getLastLoginProvider(): SocialProvider? {
        val saved = lastLoginDataStore.providerFlow.first() ?: return null

        // 저장된 문자열 → 도메인 enum 변환
        return SocialProvider.entries.firstOrNull { it.name == saved }
    }

    private companion object {
        const val LOGIN_FAILED_MESSAGE = "로그인하지 못했어요. 잠시 후 다시 시도해 주세요."
    }
}
