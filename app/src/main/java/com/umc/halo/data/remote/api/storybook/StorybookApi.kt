package com.umc.halo.data.remote.api.storybook

import com.umc.halo.core.network.BaseResponse
import com.umc.halo.data.remote.dto.response.storybook.RecommendedStorybookListResponse
import com.umc.halo.data.remote.dto.response.storybook.StorybookListResponse
import retrofit2.http.GET

/**
 * 스토리북 목록 화면(하단바 '스토리북' 탭) 서버 API
 */
interface StorybookApi {

    // 스토리북 10권 전체 + 진행 상태 + 상황별 추천 5개 카테고리
    @GET("api/v1/storybooks")
    suspend fun getStorybooks(): BaseResponse<StorybookListResponse>

    // 온보딩 목적 태그 기반 맞춤 추천 2권
    @GET("api/v1/storybooks/recommended")
    suspend fun getRecommendedStorybooks(): BaseResponse<RecommendedStorybookListResponse>
}
