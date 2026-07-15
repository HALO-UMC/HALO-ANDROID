package com.umc.halo.domain.repository.auth

import com.umc.halo.domain.model.auth.LoginResult
import com.umc.halo.domain.model.auth.SocialProvider

/**
 * 인증 저장소 인터페이스 (도메인 계층)
 * 구현체(AuthRepositoryImpl)는 data 계층에 둔다.
 */
interface AuthRepository {

    /**
     * 소셜 로그인.
     * providerToken(OIDC idToken)을 서버에 보내 검증/가입 후 서버 토큰을 발급받아 저장하고,
     * 화면 이동에 필요한 결과를 반환한다.
     *
     * @throws Throwable 네트워크 오류 또는 서버 실패 응답
     */
    suspend fun login(provider: SocialProvider, providerToken: String): LoginResult
}
