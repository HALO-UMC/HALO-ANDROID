package com.umc.halo.data.remote.api.home

import com.umc.halo.core.network.BaseResponse
import com.umc.halo.data.remote.dto.response.home.HomeResponse
import retrofit2.http.Body
import retrofit2.http.GET

interface HomeApi {

    //홈화면 정보 불러오기
    @GET("/api/v1/home")
    suspend fun getHome(): BaseResponse<HomeResponse>
}