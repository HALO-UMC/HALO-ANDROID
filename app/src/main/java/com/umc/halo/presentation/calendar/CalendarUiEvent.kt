package com.umc.halo.presentation.calendar

/** 캘린더 화면 이벤트 */
sealed interface CalendarUiEvent {
    // 달 이동
    data object OnPrevMonthClicked : CalendarUiEvent
    data object OnNextMonthClicked : CalendarUiEvent

    /** 날짜 칸 클릭 → 그 날 기록 모달 열기(기록 없으면 빈 모달) */
    data class OnDayClicked(val day: Int) : CalendarUiEvent

    /** 모달 닫기(X · 바깥 영역) */
    data object OnDismissModal : CalendarUiEvent

    /** 빈 모달 '시작하기' & 하단 '바로 시작하기' → 스토리북 목록(전체탭) */
    data object OnStartRecordingClicked : CalendarUiEvent

    /** 모달 '완성 스토리북' 카드 클릭 → 테마함 */
    data class OnCompletedStorybookClicked(val storybookId: Long) : CalendarUiEvent

    /**
     * 모달 '장 기록중'(그 날 완료한 장) 카드 클릭 → 그 장의 완료 결과 화면(chapter_result)
     * 이동에는 장 고유 id 가 필요
     */
    data class OnChapterClicked(val storybookId: Long, val chapterId: Long) : CalendarUiEvent

    /** 하단 요약 '자세히 보러가기' → 테마함 */
    data object OnSummaryDetailClicked : CalendarUiEvent
}
