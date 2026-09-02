package com.umc.halo.core.logging

import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

class FireBaseCrashlyticsLogger @Inject constructor(
    private val crashlytics: FirebaseCrashlytics
): CrashlyticsLogger {
    override fun log(message: String) {
        crashlytics.log(message)
    }

    override fun setCustomKey(key: String, value: String) {
        crashlytics.setCustomKey(key,value)
    }

    override fun setUserId(userId: String) {
        crashlytics.setUserId(userId)
    }

    override fun recordException(
        throwable: Throwable,
        params: Map<String, String>
    ) {
        params.forEach { (k, v) -> crashlytics.setCustomKey(k, v) }
        crashlytics.recordException(throwable)
    }

}
