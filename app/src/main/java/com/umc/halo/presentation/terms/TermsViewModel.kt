package com.umc.halo.presentation.terms

import com.umc.halo.domain.model.terms.TermsAgreement
import com.umc.halo.presentation.base.BaseViewModel

/**
 * 약관동의 화면 ViewModel
 *
 * 서버 연동 전까지 더미 약관 목록
 * 네트워크 연동 시 약관 목록/전문을 API 로 받아오면서 Repository 를 주입하고 @HiltViewModel 로 승격
 */
class TermsViewModel : BaseViewModel<TermsUiState, TermsUiEvent>(TermsUiState()) {

    override fun onEvent(event: TermsUiEvent) {
        when (event) {
            // 전체 동의: 이미 전부 동의면 전부 해제 아니면 전부 동의
            TermsUiEvent.AllAgreeToggled -> updateState {
                copy(agreedIds = if (isAllAgreed) emptySet() else terms.map { it.id }.toSet())
            }

            // 개별 약관 체크 토글
            is TermsUiEvent.TermToggled -> updateState {
                copy(
                    agreedIds = if (event.id in agreedIds) agreedIds - event.id
                    else agreedIds + event.id
                )
            }

            // 상세 화면 열기
            is TermsUiEvent.TermDetailClicked -> updateState {
                copy(selectedTermId = event.id)
            }

            // 상세 화면에서 뒤로가기 (동의 없이 닫기)
            TermsUiEvent.DetailDismissed -> updateState {
                copy(selectedTermId = null)
            }

            // 상세 화면 '동의' → 해당 약관 동의 + 목록으로 복귀
            is TermsUiEvent.DetailAgreeClicked -> updateState {
                copy(
                    agreedIds = agreedIds + event.id,
                    selectedTermId = null
                )
            }
        }
    }

    init {
        // [더미] 세부 약관 4개 — 서버 연동 시 API 응답으로 교체
        updateState { copy(terms = dummyTerms) }
    }

    private companion object {
        // 상세 화면 본문 더미 (실제 약관 전문으로 교체 예정)
        private const val DUMMY_CONTENT =
            "더미 데이터입니다."

        val dummyTerms = listOf(
            TermsAgreement(
                id = "terms_of_service",
                title = "개인정보 동의",
                required = true,
                lastUpdated = "26.06.13",
                detailHeading = "개인정보 약관",
                detailContent = DUMMY_CONTENT
            ),
            TermsAgreement(
                id = "privacy_policy",
                title = "개인정보 동의",
                required = true,
                lastUpdated = "26.06.13",
                detailHeading = "개인정보 약관",
                detailContent = DUMMY_CONTENT
            ),
            TermsAgreement(
                id = "location_terms",
                title = "개인정보 동의",
                required = true,
                lastUpdated = "26.06.13",
                detailHeading = "개인정보 약관",
                detailContent = DUMMY_CONTENT
            ),
            TermsAgreement(
                id = "marketing_terms",
                title = "개인정보 동의",
                required = true,
                lastUpdated = "26.06.13",
                detailHeading = "개인정보 약관",
                detailContent = DUMMY_CONTENT
            )
        )
    }
}
