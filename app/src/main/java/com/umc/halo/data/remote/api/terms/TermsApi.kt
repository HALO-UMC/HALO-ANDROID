package com.umc.halo.data.remote.api.terms

import com.umc.halo.core.network.BaseResponse
import com.umc.halo.data.remote.dto.request.terms.TermsAgreementRequest
import com.umc.halo.data.remote.dto.response.terms.TermsAgreedResponse
import com.umc.halo.data.remote.dto.response.terms.TermsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * 약관 관련 서버 API
 */
interface TermsApi {

    // 약관 목록 (필수/선택 구분 포함). 인증 없이도 조회 가능
    @GET("api/v1/terms")
    suspend fun getTerms(): BaseResponse<List<TermsResponse>>

    // 필수 약관 전체 동의 여부. 로그인/스플래시에서 약관 화면을 띄울지 판단하는 기준
    @GET("api/v1/terms/agreements")
    suspend fun getTermsAgreed(): BaseResponse<TermsAgreedResponse>

    // 약관 동의 저장. 필수 약관이 하나라도 빠지면 서버가 TERMS400_1 로 reject
    @POST("api/v1/terms/agreements")
    suspend fun agreeTerms(@Body request: TermsAgreementRequest): BaseResponse<Unit>
}
