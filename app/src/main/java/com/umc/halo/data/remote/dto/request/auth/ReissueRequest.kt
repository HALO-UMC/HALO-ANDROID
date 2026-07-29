package com.umc.halo.data.remote.dto.request.auth

/**
 * POST /api/v1/auth/reissue 요청 body
 *
 * @param refreshToken 저장해둔 Refresh Token (만료 2주)
 */
data class ReissueRequest(
    val refreshToken: String
)
