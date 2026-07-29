package com.umc.halo.data.remote.dto.response.auth

/**
 * POST /api/v1/auth/reissue 응답
 *
 * 서버가 refresh 토큰도 함께 회전(rotate)시키므로 두 토큰을 모두 새로 저장해야 한다.
 *
 * TODO: 백엔드에 termsAgreed / onboardingCompleted 필드 추가를 요청둠
 *  추가되면 스플래시에서 terms·onboarding 조회 API 2번을 생략할 수 있음
 *  (ResolveStartDestinationUseCase 만 수정하면 됨)
 */
data class ReissueResponse(
    val accessToken: String,
    val refreshToken: String
)
