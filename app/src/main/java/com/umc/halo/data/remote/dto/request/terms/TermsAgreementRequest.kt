package com.umc.halo.data.remote.dto.request.terms

/**
 * POST /api/v1/terms/agreements 요청 body
 *
 * 동의한 것만 보내는 게 아니라 약관 전체를 isAgreed 와 함께 보냄
 */
data class TermsAgreementRequest(
    val agreements: List<TermAgreementItem>
)

data class TermAgreementItem(
    val termId: Long,
    val isAgreed: Boolean
)
