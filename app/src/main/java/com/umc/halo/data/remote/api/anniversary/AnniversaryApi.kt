package com.umc.halo.data.remote.api.anniversary

import com.umc.halo.core.network.BaseResponse
import com.umc.halo.data.remote.dto.request.anniversary.AnniversaryDeleteRequest
import com.umc.halo.data.remote.dto.request.anniversary.AnniversarySaveRequest
import com.umc.halo.data.remote.dto.response.anniversary.AnniversaryIdResponse
import com.umc.halo.data.remote.dto.response.anniversary.AnniversaryListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface AnniversaryApi {
    @GET("api/v1/anniversary")
    suspend fun getAnniversaries(): BaseResponse<AnniversaryListResponse>

    @POST("api/v1/anniversary")
    suspend fun createAnniversary(
        @Body request: AnniversarySaveRequest
    ): BaseResponse<AnniversaryIdResponse>

    @PATCH("api/v1/anniversary/{anniversaryId}")
    suspend fun updateAnniversary(
        @Path("anniversaryId") anniversaryId: Long,
        @Body request: AnniversarySaveRequest
    ): BaseResponse<AnniversaryIdResponse>

    @HTTP(method = "DELETE", path = "api/v1/anniversary", hasBody = true)
    suspend fun deleteAnniversaries(
        @Body request: AnniversaryDeleteRequest
    ): BaseResponse<Unit>
}
