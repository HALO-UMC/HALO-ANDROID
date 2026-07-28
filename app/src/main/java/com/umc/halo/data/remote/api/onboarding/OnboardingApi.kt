package com.umc.halo.data.remote.api.onboarding

import com.umc.halo.core.network.BaseResponse
import com.umc.halo.data.remote.dto.response.onboarding.OnboardingStatusResponse
import retrofit2.http.GET

/**
 * 온보딩 관련 서버 API
 *
 * TODO(온보딩 담당): 온보딩 정보 저장 API 는 여기에 추가
 *  POST api/v1/onboarding  (body: step 1~5 + 그 단계의 입력값)
 *  이 파일은 로그인 흐름 분기에 필요한 '상태 조회'만 먼저 만들어 둠
 */
interface OnboardingApi {

    // 온보딩 진행 상태. 앱 실행 시 온보딩 화면으로 보낼지 홈으로 보낼지 판단하는 데 사용
    @GET("api/v1/onboarding/status")
    suspend fun getStatus(): BaseResponse<OnboardingStatusResponse>
}
