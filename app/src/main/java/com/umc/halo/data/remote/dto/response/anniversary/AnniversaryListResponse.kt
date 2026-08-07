package com.umc.halo.data.remote.dto.response.anniversary

data class AnniversaryListResponse(
    val upcomingAnniversaries: List<UpcomingAnniversaryResponse>?,
    val myAnniversaries: List<MyAnniversaryResponse>?,
    val commonAnniversaries: List<CommonAnniversaryResponse>?
)

data class UpcomingAnniversaryResponse(
    val anniversaryId: Long?,
    val commonAnniversaryId: Long?,
    val title: String?,
    val anniversaryDate: String?,
    val dDay: Int?
)

data class MyAnniversaryResponse(
    val anniversaryId: Long?,
    val title: String?,
    val anniversaryDate: String?,
    val displayDate: String?,
    val upcomingDate: String?,
    val solarDate: String?,
    val isLunar: Boolean?,
    val isRepeated: Boolean?,
    val sevenDaysAlarmEnabled: Boolean?,
    val dayAlarmEnabled: Boolean?,
    val memo: String?
)

data class CommonAnniversaryResponse(
    val commonAnniversaryId: Long?,
    val title: String?,
    val month: Int?,
    val day: Int?,
    val isLunar: Boolean?,
    val sevenDaysAlarmEnabled: Boolean?,
    val dayAlarmEnabled: Boolean?,
    val memo: String?
)
