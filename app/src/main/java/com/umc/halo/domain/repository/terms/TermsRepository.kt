package com.umc.halo.domain.repository.terms

import com.umc.halo.domain.model.terms.TermsAgreedStatus
import com.umc.halo.domain.model.terms.TermsAgreement

/**
 * 약관 저장소 인터페이스 (도메인 계층)
 */
interface TermsRepository {

    /** 약관 목록 (필수/선택 구분 포함) */
    suspend fun getTerms(): List<TermsAgreement>

    /**
     * 이 사용자의 약관 동의 현황
     * - 로그인 직후, 앱 실행(스플래시): 약관 화면을 띄울지 판단 → [TermsAgreedStatus.allRequiredAgreed]
     * - 약관 화면: 되돌아왔을 때 체크 상태 복원 → [TermsAgreedStatus.agreedTermIds]
     *
     * @return 조회 실패 시 '미동의'(allRequiredAgreed = false) → 약관 화면을 한 번 더 보여줌
     */
    suspend fun getTermsAgreedStatus(): TermsAgreedStatus

    /**
     * 약관 동의 저장
     *
     * @param agreements termId → 동의 여부. 선택 약관의 '동의 안 함'도 함께 보냄
     * @return 저장 성공 여부
     */
    suspend fun agreeTerms(agreements: Map<Long, Boolean>): Boolean
}
