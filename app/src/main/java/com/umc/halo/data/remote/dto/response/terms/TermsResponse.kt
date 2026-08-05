package com.umc.halo.data.remote.dto.response.terms

/**
 * GET /api/v1/terms 응답 목록의 한 항목
 *
 * @param termId           약관 ID
 * @param title            약관 제목
 * @param shortDescription 약관 요약
 * @param isRequired       필수 약관 여부
 * @param description      약관 전문
 * @param updatedAt        약관 최종 수정일
 */
data class TermsResponse(
    val termId: Long,
    val title: String,
    val shortDescription: String,
    val isRequired: Boolean,
    val description: String?,
    val updatedAt: String?
)

/**
 * GET /api/v1/terms/agreements 응답
 *
 * @param termsAgreed 필수 약관 전체 동의 여부. 앱 실행 시 약관 화면 노출 여부 판단에 사용
 * @param agreements  약관별 동의 내역. 온보딩에서 약관 화면으로 되돌아왔을 때 체크 상태를 되살리는 데 사용
 *
 * [agreements] 방어용으로 nullable 로 받고 Impl 에서 orEmpty() 로 정리
 */
data class TermsAgreedResponse(
    val termsAgreed: Boolean,
    val agreements: List<TermAgreementStatusResponse>? = null
)

/**
 * GET /api/v1/terms/agreements 응답의 약관별 동의 내역 한 건
 *
 * @param termId   약관 ID
 * @param isAgreed 동의 여부
 */
data class TermAgreementStatusResponse(
    val termId: Long,
    val isAgreed: Boolean
)
