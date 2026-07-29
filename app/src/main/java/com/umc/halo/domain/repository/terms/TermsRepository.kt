package com.umc.halo.domain.repository.terms

import com.umc.halo.domain.model.terms.TermsAgreement

/**
 * 약관 저장소 인터페이스 (도메인 계층)
 */
interface TermsRepository {

    /** 약관 목록 (필수/선택 구분 포함) */
    suspend fun getTerms(): List<TermsAgreement>

    /**
     * 필수 약관에 모두 동의했는지 여부
     * 로그인 직후와 앱 실행(스플래시)에서 약관 화면을 띄울지 판단하는 기준
     *
     * @return 조회 실패 시 false
     */
    suspend fun isTermsAgreed(): Boolean

    /**
     * 약관 동의 저장
     *
     * @param agreements termId → 동의 여부. 선택 약관의 '동의 안 함'도 함께 보냄
     * @return 저장 성공 여부
     */
    suspend fun agreeTerms(agreements: Map<Long, Boolean>): Boolean
}
