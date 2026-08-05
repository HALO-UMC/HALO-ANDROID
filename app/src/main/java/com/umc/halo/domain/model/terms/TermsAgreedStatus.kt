package com.umc.halo.domain.model.terms

/**
 * 사용자의 약관 동의 현황 (GET /api/v1/terms/agreements)
 *
 * @param allRequiredAgreed 필수 약관을 전부 동의했는지.
 *   로그인/스플래시에서 약관 화면을 띄울지 판단하는 기준.
 * @param agreedTermIds 동의한 약관 id 집합. 약관 화면에 되돌아왔을 때 체크 상태를 되살리는 데 사용.
 *   조회에 실패했거나 동의한 약관이 없으면 빈 집합.
 */
data class TermsAgreedStatus(
    val allRequiredAgreed: Boolean,
    val agreedTermIds: Set<Long> = emptySet()
)
