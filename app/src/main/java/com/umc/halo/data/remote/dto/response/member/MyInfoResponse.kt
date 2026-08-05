package com.umc.halo.data.remote.dto.response.member

/**
 * GET /api/v1/members/me 응답 (내 정보 조회)
 *
 * TODO : 여기에 필요한 필드 추가(예: 마이페이지 등)
 */
data class MyInfoResponse(
    val memberId: Long,
    val name: String?,
    val gender: String?,
    val birthDate: String?,
    val provider: String?,           // KAKAO / GOOGLE
    val onboardingCompleted: Boolean?,
    val characterImageUrl: String?,
    val email: String?,
    val createdAt: String?
)
