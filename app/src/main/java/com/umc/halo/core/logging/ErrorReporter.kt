package com.umc.halo.core.logging

import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class ErrorReporter @Inject constructor(
    private val analyticsLogger: AnalyticsLogger,
    private val crashlyticsLogger: CrashlyticsLogger
) {
    fun report(
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