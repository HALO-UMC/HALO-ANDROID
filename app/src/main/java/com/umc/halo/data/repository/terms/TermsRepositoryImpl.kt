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
                lastUpdated = dto.updatedAt?.toDisplayDate().orEmpty(),
                // 상세 화면 제목은 서버에 따로 없어서 약관 제목을 그대로 사용
                detailHeading = dto.title,
                detailContent = dto.description.orEmpty()
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

    /**
     * 서버의 ISO 날짜시간을 화면 표기로 변환
     */
    private fun String.toDisplayDate(): String {
        val parts = substringBefore('T').split('-')   // 2026-06-13 → [2026, 06, 13]
        if (parts.size != 3) return this

        val (year, month, day) = parts
        if (year.length != 4) return this

        return "${year.takeLast(2)}.$month.$day"      // 26.06.13
    }
}
