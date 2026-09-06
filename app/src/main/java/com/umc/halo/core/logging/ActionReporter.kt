package com.umc.halo.core.logging

import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

/**
 * 화면(screen)에서 일어난 액션(action) 하나의 성공/실패를 Firebase로 리포트
 */
class ActionReporter @Inject constructor(
    private val analyticsLogger: AnalyticsLogger,
    private val crashlyticsLogger: CrashlyticsLogger
) {
    /**
     * 특정 화면(screen)에서의 액션(action)이 성공했을 때 Analytics 이벤트로 전송
     * 이벤트명은 "{screen}_{action}_success" — Firebase 이벤트명 40자 제한을 넘지 않도록
     * screen/action 을 지어야 함
     */
    fun reportSuccess(
        screen: String,
        action: String,
        params: Map<String, Any> = emptyMap()
    ) {
        analyticsLogger.logEvent("${screen}_${action}_success", params)
    }

    fun reportFailure(
        throwable: Throwable,
        screen: String,
        action: String,
        expectedCodes: Set<Int> = DEFAULT_EXPECTED_CODES
    ) {
        when (throwable) {
            is HttpException -> {
                val code = throwable.code()
                if (code in expectedCodes) {
                    analyticsLogger.logEvent(
                        "${screen}_${action}_failed",
                        mapOf("reason" to "client_error", "code" to code.toString())
                    )
                } else {
                    crashlyticsLogger.recordException(
                        throwable,
                        mapOf("screen" to screen, "action" to action, "code" to code.toString())
                    )
                }
            }
            is IOException -> {
                crashlyticsLogger.recordException(
                    throwable,
                    mapOf("screen" to screen, "action" to action, "type" to "network")
                )
            }
            else -> {
                crashlyticsLogger.recordException(
                    throwable,
                    mapOf("screen" to screen, "action" to action)
                )
            }
        }
    }

    companion object {
        private val DEFAULT_EXPECTED_CODES = setOf(400, 401, 403, 404, 409, 422)
    }
}
