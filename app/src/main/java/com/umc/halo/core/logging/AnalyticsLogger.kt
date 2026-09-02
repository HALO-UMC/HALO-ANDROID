package com.umc.halo.core.logging

interface AnalyticsLogger {
    fun logScreenView(screenName: String)
    fun logEvent(name: String, params: Map<String, Any> = emptyMap())
}