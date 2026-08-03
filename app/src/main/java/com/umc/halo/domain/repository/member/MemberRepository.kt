package com.umc.halo.domain.repository.member

import com.umc.halo.domain.model.member.MemberInfo

/**
 * 회원 저장소 인터페이스 (도메인 계층)
 */
interface MemberRepository {

    /**
     * 로그인한 회원의 기본 정보 조회
     * 현재는 화면 문구에 넣을 이름을 얻는 용도
     * TODO : 필요한 필드를 여기에 추가
     */
    suspend fun getMyInfo(): MemberInfo

    /**
     * 회원 탈퇴
     * 서버 계정/온보딩 상태를 삭제하고 로컬 토큰을 지움
     *
     * @return 서버 탈퇴 성공 여부. 실패하면 로컬 토큰은 지우지 않음
     */
    suspend fun withdraw(): Boolean
}
