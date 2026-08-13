package com.umc.halo.core.network

import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

/**
 * API 실패를 사용자에게 보여줄 문구로 바꾸는 공통 유틸
 *
 * 실패는 세 갈래로 나누어 처리
 * 1. [IOException]  = 요청이 서버에 가지 못함(비행기 모드, DNS 실패. 타임아웃)
 * 2. [HttpException] = 서버가 응답은 했고 4xx/5xx → 서버가 준 실패 사유를 보여줌
 * 3. 그 외          = 예상 못 한 예외 → 화면이 정한 기본 문구
 */

/** 네트워크가 끊겼을 때 문구 */
const val NETWORK_ERROR_MESSAGE = "인터넷 연결을 확인해 주세요."

/**
 * 예외를 사용자 문구로 변환
 *
 * @param defaultMessage 서버가 사유를 안 줬거나 알 수 없는 예외일 때 쓸 화면별 기본 문구
 */
internal fun Throwable.toApiErrorMessage(defaultMessage: String): String = when (this) {
    is IOException -> NETWORK_ERROR_MESSAGE

    is HttpException -> response()
        ?.errorBody()
        ?.string()
        ?.extractApiErrorMessage()
        ?: defaultMessage

    else -> message?.takeIf { it.isNotBlank() } ?: defaultMessage
}

/**
 * ViewModel 이 잡은 예외에서 사용자에게 보여줄 에러 메세지 추출. 없으면 null
 *
 * [toApiErrorMessage] 는 마지막 가지에서 예외의 message 를 그대로 사용.
 * - [IOException] : SDK 단계의 네트워크 단절
 * - [IllegalStateException] : 레포지토리가 만든 문구
 */
internal fun Throwable.toUserMessageOrNull(): String? = when (this) {
    is IOException -> NETWORK_ERROR_MESSAGE
    is IllegalStateException -> message?.takeIf { it.isNotBlank() }
    else -> null
}

/**
 * 2xx 로 왔지만 `isSuccess = false` 인 응답의 문구
 * (HTTP 상태코드는 성공인데 서버가 본문으로 실패를 알려주는 경우)
 */
internal fun BaseResponse<*>.toApiErrorMessage(defaultMessage: String): String =
    message.takeIf { it.isNotBlank() } ?: defaultMessage

/**
 * 에러 응답 (body)에서 에러 문구 추출
 */
private fun String.extractApiErrorMessage(): String? =
    runCatching {
        val json = JSONObject(this)
        when (val result = json.opt("result")) {
            is JSONObject -> {
                val keys = result.keys()
                if (keys.hasNext()) {
                    result.optString(keys.next()).takeIf { it.isNotBlank() }
                } else {
                    null
                }
            }

            is String -> result.takeIf { it.isNotBlank() }
            else -> null
        } ?: json.optString("message").takeIf { it.isNotBlank() }
    }.getOrNull()
