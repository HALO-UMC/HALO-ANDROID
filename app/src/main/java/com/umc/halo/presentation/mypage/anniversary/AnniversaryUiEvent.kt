package com.umc.halo.presentation.mypage.anniversary

import com.umc.halo.presentation.base.UiEvent

sealed interface AnniversaryUiEvent : UiEvent {
    data object BackClicked : AnniversaryUiEvent
    data object AddClicked : AnniversaryUiEvent
    data object SelectModeClicked : AnniversaryUiEvent
    data object DeleteSelectedClicked : AnniversaryUiEvent
    data class AnniversaryClicked(val id: Long) : AnniversaryUiEvent
    data class UpcomingClicked(val id: Long) : AnniversaryUiEvent
    data class SelectionToggled(val id: Long) : AnniversaryUiEvent

    data class TitleChanged(val title: String) : AnniversaryUiEvent
    data object DateFieldClicked : AnniversaryUiEvent
    data object PreviousMonthClicked : AnniversaryUiEvent
    data object NextMonthClicked : AnniversaryUiEvent
    data class CalendarTypeChanged(val type: AnniversaryCalendarType) : AnniversaryUiEvent
    data class DateSelected(val date: AnniversaryDate) : AnniversaryUiEvent
    data class D7AlarmChanged(val enabled: Boolean) : AnniversaryUiEvent
    data class DayAlarmChanged(val enabled: Boolean) : AnniversaryUiEvent
    data class MemoChanged(val memo: String) : AnniversaryUiEvent
    data object SaveClicked : AnniversaryUiEvent
}
