package com.umc.halo.domain.model.terms

/**
 * 세부 약관 리스트의 한 줄
 * 서버 연동 시 이 목록을 API 응답으로 대체할 예정
 *
 * @param id 약관 식별자 (동의 상태를 id로 식별함)
 * @param title 약관 목록에 보이는 약관 이름
 * @param required 필수 약관 여부
 * @param lastUpdated 약관 상세 화면 상단의 최종 업데이트 표기
 * @param detailHeading 약관 상세 화면 제목
 * @param detailContent 약관 상세 화면 본문
 */
data class TermsAgreement(
    val id: String,
    val title: String,
    val required: Boolean = true,
    val lastUpdated: String = "",
    val detailHeading: String = "",
    val detailContent: String = ""
)
