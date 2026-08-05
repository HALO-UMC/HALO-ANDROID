package com.umc.halo.presentation.mypage.anniversary

import com.umc.halo.presentation.base.UiState
import java.util.Calendar

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

    fun compactWithDayOfWeek(): String =
        "${formatted()} (${weekdayLabel()})"

    private fun weekdayLabel(): String {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, day)
        }

        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> "일"
            Calendar.MONDAY -> "월"
            Calendar.TUESDAY -> "화"
            Calendar.WEDNESDAY -> "수"
            Calendar.THURSDAY -> "목"
            Calendar.FRIDAY -> "금"
            else -> "토"
        }
    }
}

data class AnniversaryItem(
    val id: Long,
    val title: String,
    val date: AnniversaryDate,
    val calendarType: AnniversaryCalendarType = AnniversaryCalendarType.SOLAR,
    val dDayLabel: String? = null,
    val memo: String = "",
    val repeatEnabled: Boolean = true,
    val d7AlarmEnabled: Boolean = false,
    val dayAlarmEnabled: Boolean = true,
    val isOfficial: Boolean = false
)

data class AnniversaryFormState(
    val editingId: Long? = null,
    val title: String = "",
    val date: AnniversaryDate? = null,
    val calendarType: AnniversaryCalendarType = AnniversaryCalendarType.SOLAR,
    val repeatEnabled: Boolean = true,
    val d7AlarmEnabled: Boolean = false,
    val dayAlarmEnabled: Boolean = true,
    val memo: String = "",
    val isCalendarExpanded: Boolean = false,
    val visibleYear: Int = currentCalendarYear(),
    val visibleMonth: Int = currentCalendarMonth()
) {
    val canSave: Boolean
        get() = title.isNotBlank() && date != null
}

data class AnniversaryUiState(
    val mode: AnniversaryScreenMode = AnniversaryScreenMode.LIST,
    val upcomingItems: List<AnniversaryItem> = defaultUpcomingAnniversaries,
    val personalItems: List<AnniversaryItem> = defaultPersonalAnniversaries,
    val isSelectionModeActive: Boolean = false,
    val selectedIds: Set<Long> = emptySet(),
    val lastAddedId: Long? = null,
    val openedItem: AnniversaryItem? = null,
    val form: AnniversaryFormState = AnniversaryFormState(),
    val today: AnniversaryDate = currentAnniversaryDate()
) : UiState {
    val isSelectionMode: Boolean
        get() = isSelectionModeActive

    val visibleUpcomingItems: List<AnniversaryItem>
        get() = (upcomingItems + personalItems)
            .mapNotNull { item ->
                val daysUntil = item.daysUntilUpcoming(today)
                if (daysUntil in 0..7) {
                    item.copy(dDayLabel = daysUntil.toDdayLabel()) to daysUntil
                } else {
                    null
                }
            }
            .distinctBy { it.first.id }
            .sortedBy { it.second }
            .map { it.first }
}

private const val MILLIS_PER_DAY = 24L * 60L * 60L * 1000L

private fun currentCalendarYear(): Int =
    Calendar.getInstance().get(Calendar.YEAR)

private fun currentCalendarMonth(): Int =
    Calendar.getInstance().get(Calendar.MONTH) + 1

private fun currentAnniversaryDate(): AnniversaryDate {
    val calendar = Calendar.getInstance()
    return AnniversaryDate(
        year = calendar.get(Calendar.YEAR),
        month = calendar.get(Calendar.MONTH) + 1,
        day = calendar.get(Calendar.DAY_OF_MONTH)
    )
}

private fun AnniversaryDate.daysUntilNextOccurrence(from: AnniversaryDate): Int {
    val fromCalendar = Calendar.getInstance().apply {
        clear()
        set(from.year, from.month - 1, from.day)
    }
    val targetCalendar = Calendar.getInstance().apply {
        clear()
        set(from.year, month - 1, day)
    }

    if (targetCalendar.before(fromCalendar)) {
        targetCalendar.add(Calendar.YEAR, 1)
    }

    return ((targetCalendar.timeInMillis - fromCalendar.timeInMillis) / MILLIS_PER_DAY).toInt()
}

private fun AnniversaryDate.daysUntilExactDate(from: AnniversaryDate): Int {
    val fromCalendar = Calendar.getInstance().apply {
        clear()
        set(from.year, from.month - 1, from.day)
    }
    val targetCalendar = Calendar.getInstance().apply {
        clear()
        set(year, month - 1, day)
    }

    return ((targetCalendar.timeInMillis - fromCalendar.timeInMillis) / MILLIS_PER_DAY).toInt()
}

private fun AnniversaryItem.daysUntilUpcoming(from: AnniversaryDate): Int =
    if (isOfficial || repeatEnabled) {
        date.daysUntilNextOccurrence(from)
    } else {
        date.daysUntilExactDate(from)
    }

private fun AnniversaryDate.plusDays(days: Int): AnniversaryDate {
    val calendar = Calendar.getInstance().apply {
        clear()
        set(year, month - 1, day)
        add(Calendar.DAY_OF_MONTH, days)
    }
    return AnniversaryDate(
        year = calendar.get(Calendar.YEAR),
        month = calendar.get(Calendar.MONTH) + 1,
        day = calendar.get(Calendar.DAY_OF_MONTH)
    )
}

private fun Int.toDdayLabel(): String =
    if (this == 0) "D-DAY" else "D-$this"

private val defaultUpcomingAnniversaries = run {
    val today = currentAnniversaryDate()
    listOf(
        AnniversaryItem(
            id = 101,
            title = "아버지 생신",
            date = today,
            memo = "아버지 생신 챙겨드려야지!",
            isOfficial = true
        ),
        AnniversaryItem(
            id = 102,
            title = "어머니 생신",
            date = today.plusDays(7),
            memo = "어머니랑 여행을 가기로 한 날",
            isOfficial = true
        )
    )
}

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
