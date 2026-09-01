package com.umc.halo.core.logging

import javax.inject.Inject

class FireBaseAnalyticsLogger @Inject constructor(
    private val analyticsLogger: AnalyticsLogger
) {
}