package com.umc.halo.domain.repository.member

/**
 * 회원 저장소 인터페이스 (도메인 계층)
 */
interface MemberRepository {

    /**
     * 회원 탈퇴
     * 서버 계정/온보딩 상태를 삭제하고 로컬 토큰을 지움
     *
     * @return 서버 탈퇴 성공 여부. 실패하면 로컬 토큰은 지우지 않음
     */
    suspend fun withdraw(): Boolean
}
