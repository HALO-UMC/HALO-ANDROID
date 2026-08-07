package com.umc.halo.data.remote.dto.request.anniversary

data class AnniversarySaveRequest(
    val title: String,
    val anniversaryDate: String,
    val isLunar: Boolean,
    val isRepeated: Boolean,
    val sevenDaysAlarmEnabled: Boolean,
    val dayAlarmEnabled: Boolean,
    val memo: String?
)
