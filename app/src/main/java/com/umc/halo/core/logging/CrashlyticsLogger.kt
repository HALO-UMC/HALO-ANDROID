package com.umc.halo.core.logging


interface CrashlyticsLogger {
    fun log(message: String)
    fun setCustomKey(key: String, value: String)
    fun setUserId(userId: String)
    fun recordException(throwable: Throwable, params: Map<String, String> = emptyMap())
}
