package com.umc.halo.presentation.terms

/** 약관동의 화면에서 발생하는 사용자 이벤트 */
sealed interface TermsUiEvent {

    /** '전체 동의' 체크 토글 (하나라도 안 된 게 있으면 전부 동의 이미 전부 동의면 전부 해제) */
    data object AllAgreeToggled : TermsUiEvent

    /** 개별 약관 체크 토글 */
    data class TermToggled(val id: String) : TermsUiEvent

    /** 약관 행의 > 클릭 → 상세 화면 열기 */
    data class TermDetailClicked(val id: String) : TermsUiEvent

    /** 상세 화면에서 뒤로가기 → 동의 없이 목록으로 복귀 */
    data object DetailDismissed : TermsUiEvent

    /** 상세 화면의 '동의' 버튼 클릭 → 해당 약관 동의 처리 후 목록으로 복귀 */
    data class DetailAgreeClicked(val id: String) : TermsUiEvent
}
