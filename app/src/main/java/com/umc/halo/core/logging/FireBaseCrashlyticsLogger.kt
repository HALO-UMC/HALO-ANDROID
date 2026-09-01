package com.umc.halo.core.logging

import javax.inject.Inject

class FireBaseCrashlyticsLogger @Inject constructor(
    private val crashlyticsLogger: CrashlyticsLogger
) {
}
