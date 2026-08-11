package com.umc.halo.data.remote.api.member

import com.umc.halo.core.network.BaseResponse
import com.umc.halo.data.remote.dto.response.member.MyInfoResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

/**
 * 회원 관련 서버 API
 */
interface MemberApi {

    // 내 정보 조회: memberId·name·gender·birthDate·provider·onboardingCompleted·createdAt
    @GET("api/v1/members/me")
    suspend fun getMyInfo(): BaseResponse<MyInfoResponse>

    // 회원 탈퇴: 계정과 온보딩 완료 상태를 삭제 → 같은 소셜 계정으로 재가입 시 신규 사용자로 처리됨
    @DELETE("api/v1/members/me")
    suspend fun withdraw(): BaseResponse<Unit>

    @PATCH("api/v1/members/last-access-at")
    suspend fun userAccess()
}
