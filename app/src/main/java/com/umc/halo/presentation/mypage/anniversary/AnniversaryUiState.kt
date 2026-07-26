package com.umc.halo.presentation.mypage.anniversary

import com.umc.halo.presentation.base.UiState

enum class AnniversaryScreenMode {
    LIST,
    ADD,
    DETAIL,
    EDIT
}

enum class AnniversaryCalendarType(
    val label: String
) {
    SOLAR("양력"),
    LUNAR("음력")
}

data class AnniversaryDate(
    val year: Int,
    val month: Int,
    val day: Int
) {
    fun formatted(): String =
        "${year}.${month.toString().padStart(2, '0')}.${day.toString().padStart(2, '0')}"

    fun compactWithDayOfWeek(dayOfWeek: String = "금"): String =
        "${formatted()} ($dayOfWeek)"
}

data class AnniversaryItem(
    val id: Long,
    val title: String,
    val date: AnniversaryDate,
    val calendarType: AnniversaryCalendarType = AnniversaryCalendarType.SOLAR,
    val dDayLabel: String? = null,
    val memo: String = "",
    val d7AlarmEnabled: Boolean = false,
    val dayAlarmEnabled: Boolean = true,
    val isOfficial: Boolean = false
)

data class AnniversaryFormState(
    val editingId: Long? = null,
    val title: String = "",
    val date: AnniversaryDate? = null,
    val calendarType: AnniversaryCalendarType = AnniversaryCalendarType.SOLAR,
    val d7AlarmEnabled: Boolean = false,
    val dayAlarmEnabled: Boolean = true,
    val memo: String = "",
    val isCalendarExpanded: Boolean = false,
    val visibleYear: Int = 2026,
    val visibleMonth: Int = 6
) {
    val canSave: Boolean
        get() = title.isNotBlank() && date != null
}

data class AnniversaryUiState(
    val mode: AnniversaryScreenMode = AnniversaryScreenMode.LIST,
    val upcomingItems: List<AnniversaryItem> = defaultUpcomingAnniversaries,
    val personalItems: List<AnniversaryItem> = defaultPersonalAnniversaries,
    val selectedIds: Set<Long> = emptySet(),
    val lastAddedId: Long? = null,
    val openedItem: AnniversaryItem? = null,
    val form: AnniversaryFormState = AnniversaryFormState()
) : UiState {
    val isSelectionMode: Boolean
        get() = selectedIds.isNotEmpty()
}

private val defaultUpcomingAnniversaries = listOf(
    AnniversaryItem(
        id = 101,
        title = "아버지 생신",
        date = AnniversaryDate(2026, 6, 16),
        dDayLabel = "D-DAY",
        memo = "아버지 생신 챙겨드려야지!",
        isOfficial = true
    ),
    AnniversaryItem(
        id = 102,
        title = "어머니 생신",
        date = AnniversaryDate(2026, 7, 28),
        dDayLabel = "D-7",
        memo = "어머니랑 여행을 가기로 한 날",
        isOfficial = true
    )
)

private val defaultPersonalAnniversaries = listOf(
    AnniversaryItem(
        id = 1,
        title = "어머니 생신",
        date = AnniversaryDate(2026, 5, 8)
    ),
    AnniversaryItem(
        id = 2,
        title = "아버지 생신",
        date = AnniversaryDate(2026, 5, 8)
    ),
    AnniversaryItem(
        id = 3,
        title = "할아버지 생신",
        date = AnniversaryDate(2026, 5, 8)
    )
)
