package com.umc.halo.domain.model.terms

/**
 * 세부 약관 리스트의 한 줄
 *
 * @param id 약관 식별자 (서버 termId. 동의 상태를 이 id 로 식별)
 * @param title 약관 목록에 보이는 약관 이름
 * @param shortDescription 약관 요약 (서버 제공)
 * @param required 필수 약관 여부. 필수를 모두 동의해야 '다음' 버튼이 활성화
 * @param lastUpdated 약관 상세 화면 상단의 최종 업데이트 표기
 * @param detailHeading 약관 상세 화면 제목
 * @param detailContent 약관 상세 화면 본문
 *
 *  TODO: lastUpdated / detailHeading / detailContent 는 아직 서버 응답에 없음
 *  TODO: 약관 전문을 어디서 받을지는 대기 중이라 RepositoryImpl 에서 임시 값을 채워 넣음
 *  TODO: 확정되면 그 부분만 교체예정
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
