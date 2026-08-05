package com.umc.halo.domain.model.member

/**
 * 로그인한 회원의 기본 정보
 *
 * TODO : 필요한 필드 여기에 추가
 */
data class MemberInfo(
    val memberId: Long,
    val name: String,
    val gender: String?,
    val birthDate: String?,
    val provider: String?,
    val onboardingCompleted: Boolean,
    val characterImageUrl: String?,
    val email: String?,
    val createdAt: String?
)
