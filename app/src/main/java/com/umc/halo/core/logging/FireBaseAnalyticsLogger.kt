package com.umc.halo.core.logging

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import javax.inject.Inject

class FireBaseAnalyticsLogger @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
): AnalyticsLogger {
    override fun logScreenView(screenName: String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        }
    }

    override fun logEvent(
        name: String,
        params: Map<String, Any>
    ) {
        firebaseAnalytics.logEvent(name) {
            params.forEach { (k, v) ->
                when (v) {
                    is String -> param(k, v)
                    is Long -> param(k, v)
                    is Double -> param(k, v)
                }
            }
        }
    }
}