package com.umc.halo.data.remote.dto.response.terms

/**
 * GET /api/v1/terms 응답 목록의 한 항목
 *
 * ⚠️ 약관 전문(본문)과 '최종 업데이트' 날짜는 이 응답에 없다.
 *  별도 조회 API 인지 / 웹뷰 링크인지 기획 확인 대기 중이라, 화면에서는 임시 문구를 쓴다.
 */
data class TermsResponse(
    val termId: Long,
    val title: String,
    val shortDescription: String,
    val isRequired: Boolean
)

/**
 * GET /api/v1/terms/agreements 응답
 *
 * @param termsAgreed 필수 약관 전체 동의 여부. 앱 실행 시 약관 화면 노출 여부 판단에 사용한다.
 */
data class TermsAgreedResponse(
    val termsAgreed: Boolean
)
