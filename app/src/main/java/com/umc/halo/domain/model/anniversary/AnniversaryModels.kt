package com.umc.halo.domain.model.anniversary

data class AnniversaryOverview(
    val upcomingAnniversaries: List<UpcomingAnniversary>,
    val myAnniversaries: List<MyAnniversary>,
    val commonAnniversaries: List<CommonAnniversary>
)

data class UpcomingAnniversary(
    val anniversaryId: Long?,
    val commonAnniversaryId: Long?,
    val title: String,
    val anniversaryDate: String,
    val dDay: Int
)

data class MyAnniversary(
    val anniversaryId: Long,
    val title: String,
    val anniversaryDate: String,
    val displayDate: String?,
    val isLunar: Boolean,
    val isRepeated: Boolean,
    val sevenDaysAlarmEnabled: Boolean,
    val dayAlarmEnabled: Boolean,
    val memo: String?
)

data class CommonAnniversary(
    val commonAnniversaryId: Long,
    val title: String,
    val month: Int,
    val day: Int,
    val isLunar: Boolean,
    val sevenDaysAlarmEnabled: Boolean,
    val dayAlarmEnabled: Boolean,
    val memo: String?
)

data class AnniversarySaveForm(
    val title: String,
    val anniversaryDate: String,
    val isLunar: Boolean,
    val isRepeated: Boolean,
    val sevenDaysAlarmEnabled: Boolean,
    val dayAlarmEnabled: Boolean,
    val memo: String?
)
