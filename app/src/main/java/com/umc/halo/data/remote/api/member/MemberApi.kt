package com.umc.halo.data.remote.api.member

import com.umc.halo.core.network.BaseResponse
import retrofit2.http.DELETE

/**
 * 회원 관련 서버 API
 *
 * TODO(마이페이지 담당): 내 정보 조회가 필요하면 여기에 추가
 *  GET api/v1/members/me → memberId·name·gender·birthDate·provider·onboardingCompleted·createdAt
 */
interface MemberApi {

    // 회원 탈퇴: 계정과 온보딩 완료 상태를 삭제 → 같은 소셜 계정으로 재가입 시 신규 사용자로 처리됨
    @DELETE("api/v1/members/me")
    suspend fun withdraw(): BaseResponse<Unit>
}
