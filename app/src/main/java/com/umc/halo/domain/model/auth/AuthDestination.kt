package com.umc.halo.domain.model.auth

/**
 * 로그인 흐름에서 사용자를 보내야 할 화면
 *
 *   로그인 → 필수약관 동의 → 온보딩 완료 → 홈
 *
 */
enum class AuthDestination {
    /** 토큰이 없거나 만료됨 */
    LOGIN,

    /** 로그인은 됐지만 필수 약관 미동의 */
    TERMS,

    /** 약관까지 끝났지만 온보딩 미완료 */
    ONBOARDING,

    /** 모든 게이트 통과 */
    HOME
}
