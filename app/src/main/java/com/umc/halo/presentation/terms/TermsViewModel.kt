package com.umc.halo.presentation.terms

import androidx.lifecycle.viewModelScope
import com.umc.halo.domain.model.auth.AuthDestination
import com.umc.halo.domain.model.terms.TermsAgreedStatus
import com.umc.halo.domain.model.terms.TermsAgreement
import com.umc.halo.domain.repository.auth.AuthRepository
import com.umc.halo.domain.repository.terms.TermsRepository
import com.umc.halo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 약관동의 화면 ViewModel
 *
 * 약관 목록은 서버에서 받아오고(필수/선택 구분 포함) '다음'을 누르면 동의 내역을 서버에 저장
 * 저장이 성공해야 온보딩으로 넘어감 (저장 실패 상태로 넘어가면 다음 실행 때 약관이 다시 뜸)
 */
@HiltViewModel
class TermsViewModel @Inject constructor(
    private val termsRepository: TermsRepository,
    private val authRepository: AuthRepository
) : BaseViewModel<TermsUiState, TermsUiEvent>(TermsUiState()) {

    init {
        loadTerms()
    }

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

            TermsUiEvent.NextClicked -> submitAgreements()
            TermsUiEvent.BackClicked -> cancelSignUp()

            TermsUiEvent.NavigationHandled -> updateState { copy(destination = null) }
            TermsUiEvent.ErrorShown -> updateState { copy(errorMessage = null) }
        }
    }

    private fun loadTerms() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            // 약관 목록과 동의 현황은 서로 다른 API라서 두 개를 동시에 요청
            val termsDeferred = async { runCatching { termsRepository.getTerms() } }
            val agreedStatusDeferred = async { termsRepository.getTermsAgreedStatus() }

            val termsResult = termsDeferred.await()
            val agreedStatus = agreedStatusDeferred.await()

            termsResult
                .onSuccess { terms ->
                    updateState {
                        copy(
                            terms = terms,
                            agreedIds = restoreAgreedIds(terms, agreedStatus)
                        )
                    }
                }
                .onFailure { updateState { copy(errorMessage = LOAD_FAILED_MESSAGE) } }

            updateState { copy(isLoading = false) }
        }
    }

    /**
     * 화면에 들어올 때 미리 체크해둘 약관
     */
    private fun restoreAgreedIds(
        terms: List<TermsAgreement>,
        agreedStatus: TermsAgreedStatus
    ): Set<Long> = when {
        // null 이 아니면 서버가 약관별 내역을 알려줬다는 뜻
        agreedStatus.agreedTermIds != null -> agreedStatus.agreedTermIds

        agreedStatus.allRequiredAgreed ->
            terms.filter { it.required }.map { it.id }.toSet()

        else -> emptySet()
    }

    /**
     * 동의 내역 저장
     * 동의한 것만 보내는 게 아니라 약관 전체를 동의 여부와 함께 보냄
     */
    private fun submitAgreements() {
        if (currentState.isSubmitting) return

        viewModelScope.launch {
            updateState { copy(isSubmitting = true, errorMessage = null) }

            val agreements = currentState.terms.associate { term ->
                term.id to (term.id in currentState.agreedIds)
            }
            val success = runCatching { termsRepository.agreeTerms(agreements) }
                .getOrDefault(false)

            updateState {
                if (success) {
                    copy(isSubmitting = false, destination = AuthDestination.ONBOARDING)
                } else {
                    copy(isSubmitting = false, errorMessage = SUBMIT_FAILED_MESSAGE)
                }
            }
        }
    }

    /**
     * 상단바 뒤로가기 = 가입 중단
     *
     * 이 시점에는 서버 토큰을 이미 받았지만 아직 약관에 동의하지 않은 상태
     * 토큰만 남겨두고 로그인 화면으로 돌아가면 '로그인은 됐는데 로그인 화면'인 상태가 되므로
     * 로그아웃해서 처음부터 다시 시작
     */
    private fun cancelSignUp() {
        viewModelScope.launch {
            authRepository.logout()
            updateState { copy(destination = AuthDestination.LOGIN) }
        }
    }

    private companion object {
        // TODO: 에러 문구/표시 방식은 디자인 확정 후 교체
        const val LOAD_FAILED_MESSAGE = "약관을 불러오지 못했어요. 잠시 후 다시 시도해 주세요."
        const val SUBMIT_FAILED_MESSAGE = "약관 동의 저장에 실패했어요. 잠시 후 다시 시도해 주세요."
    }
}
