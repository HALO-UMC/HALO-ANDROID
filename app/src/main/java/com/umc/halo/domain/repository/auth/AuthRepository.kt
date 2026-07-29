package com.umc.halo.domain.repository.auth

import com.umc.halo.domain.model.auth.LoginResult
import com.umc.halo.domain.model.auth.SocialProvider

/**
 * 인증 저장소 인터페이스 (도메인 계층)
 * 구현체(AuthRepositoryImpl)는 data 계층에 둔다.
 */
interface AuthRepository {

    /**
     * 소셜 로그인
     * providerToken(OIDC idToken)을 서버에 보내 검증/가입 후 서버 토큰을 발급받아 저장하고
     * 화면 이동에 필요한 결과를 반환
     *
     * @throws Throwable 네트워크 오류 또는 서버 실패 응답
     */
    suspend fun login(provider: SocialProvider, providerToken: String): LoginResult

    /**
     * 저장된 refreshToken 으로 토큰을 재발급 (자동 로그인)
     *
     * @return 성공하면 true(새 토큰 저장 완료)
     *         저장된 토큰이 없거나 refreshToken 이 만료됐으면 false 이고 이때 로컬 토큰은 정리
     *         → 호출한 쪽은 false 면 로그인 화면으로 보냄
     */
    suspend fun reissue(): Boolean

    /**
     * 로그아웃
     * 서버의 refreshToken 을 무효화하고 로컬 토큰을 지움
     *
     * 서버 호출이 실패하더라도 로컬 토큰은 반드시 지움
     */
    suspend fun logout()
}
