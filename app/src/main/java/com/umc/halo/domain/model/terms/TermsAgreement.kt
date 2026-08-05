package com.umc.halo.domain.model.terms

/**
 * 세부 약관 리스트의 한 줄
 *
 * @param id 약관 식별자 (서버 termId. 동의 상태를 이 id 로 식별)
 * @param title 약관 목록에 보이는 약관 이름
 * @param shortDescription 약관 요약 (서버 제공)
 * @param required 필수 약관 여부. 필수를 모두 동의해야 '다음' 버튼이 활성화
 * @param lastUpdated 약관 상세 화면 상단의 최종 업데이트 표기
 * @param detailHeading 약관 상세 화면 제목. 서버에 대응 필드가 없어 title 을 그대로 사용
 * @param detailContent 약관 상세 화면 본문
 */
data class TermsAgreement(
    val id: Long,
    val title: String,
    val shortDescription: String = "",
    val required: Boolean = true,
    val lastUpdated: String = "",
    val detailHeading: String = "",
    val detailContent: String = ""
)
