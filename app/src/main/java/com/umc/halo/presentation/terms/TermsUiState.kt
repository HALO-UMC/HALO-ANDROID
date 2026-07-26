package com.umc.halo.presentation.terms

import com.umc.halo.domain.model.terms.TermsAgreement

/**
 * 약관동의 화면 상태
 *
 * @param terms 세부 약관 목록
 * @param agreedIds 현재 동의한 약관 id 집합
 * @param selectedTermId null 이면 목록 화면 값이 있으면 그 약관의 상세 화면을 표시
 */
data class TermsUiState(
    val terms: List<TermsAgreement> = emptyList(),
    val agreedIds: Set<String> = emptySet(),
    val selectedTermId: String? = null
) {
    /** 특정 약관 동의 여부 */
    fun isAgreed(id: String): Boolean = id in agreedIds

    /** 전체 동의 상태 (모든 약관에 동의) → '전체 동의' 행의 체크 표시에 사용 */
    val isAllAgreed: Boolean
        get() = terms.isNotEmpty() && terms.all { it.id in agreedIds }

    /** '다음' 버튼 활성화 조건 = 모든 '필수' 약관에 동의 */
    val isNextEnabled: Boolean
        get() = terms.filter { it.required }.all { it.id in agreedIds }

    /** 현재 상세로 보고 있는 약관 (없으면 null = 메인 목록 화면) */
    val selectedTerm: TermsAgreement?
        get() = terms.find { it.id == selectedTermId }
}
