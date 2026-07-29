package com.umc.halo.data.repository.terms

import com.umc.halo.data.remote.api.terms.TermsApi
import com.umc.halo.data.remote.dto.request.terms.TermAgreementItem
import com.umc.halo.data.remote.dto.request.terms.TermsAgreementRequest
import com.umc.halo.domain.model.terms.TermsAgreement
import com.umc.halo.domain.repository.terms.TermsRepository
import javax.inject.Inject

/**
 * TermsRepository 구현체
 * 서버 호출(TermsApi) → DTO 를 도메인 모델로 변환
 */
class TermsRepositoryImpl @Inject constructor(
    private val termsApi: TermsApi
) : TermsRepository {

    override suspend fun getTerms(): List<TermsAgreement> {
        val response = termsApi.getTerms()
        val result = response.result
        if (!response.isSuccess || result == null) {
            error("약관 목록 조회 실패 (code=${response.code}, message=${response.message})")
        }

        return result.map { dto ->
            TermsAgreement(
                id = dto.termId,
                title = dto.title,
                shortDescription = dto.shortDescription,
                required = dto.isRequired,
                // TODO: 아래 3개는 서버 응답에 없는 값이라 임시로 채움
                //  약관 전문을 어디서 받을지(별도 API / 웹뷰 / 앱 내장) 확정되면 이 부분만 교체 예정
                lastUpdated = PLACEHOLDER_LAST_UPDATED,
                detailHeading = dto.title,
                detailContent = dto.shortDescription
            )
        }
    }

    override suspend fun isTermsAgreed(): Boolean =
        runCatching { termsApi.getTermsAgreed() }
            .getOrNull()
            ?.takeIf { it.isSuccess }
            ?.result
            ?.termsAgreed
            ?: false   // 조회 실패 시 '미동의'로 → 약관 화면을 한 번 더 보여줌

    override suspend fun agreeTerms(agreements: Map<Long, Boolean>): Boolean {
        val request = TermsAgreementRequest(
            agreements = agreements.map { (termId, isAgreed) ->
                TermAgreementItem(termId = termId, isAgreed = isAgreed)
            }
        )

        return runCatching { termsApi.agreeTerms(request) }
            .getOrNull()
            ?.isSuccess == true
    }

    private companion object {
        const val PLACEHOLDER_LAST_UPDATED = "26.06.13"
    }
}
