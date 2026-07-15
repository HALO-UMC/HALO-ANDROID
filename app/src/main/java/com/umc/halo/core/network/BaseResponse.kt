package com.umc.halo.core.network

/**
 * 서버 공통 응답 래퍼
 *
 * API 명세서 - { isSuccess, code, message, result } 형식으로 작성
 * result 는 API 마다 다른 실제 payload 이며 실패 응답에서는 null 일 수 있어 nullable
 * 다른 도메인 API에서 재사용 가능
 */
data class BaseResponse<T>(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: T?
)
