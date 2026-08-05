package com.umc.halo.data.remote.api.relationship

import com.umc.halo.core.network.BaseResponse
import com.umc.halo.data.remote.dto.response.relationship.RelationshipInfoResponse
import retrofit2.http.GET

interface RelationshipApi {
    @GET("api/v1/relationships")
    suspend fun getRelationshipInfo(): BaseResponse<RelationshipInfoResponse>
}
