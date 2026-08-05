package com.umc.halo.domain.model.calendar

/**
 * 날짜 칸에 찍히는 서비스 로고 종류
 *  - [PAGE_COMPLETED]      : 그 날 어떤 스토리북의 한 장(챕터)을 완료 (연한 주황)
 *  - [STORYBOOK_COMPLETED] : 그 날 어떤 스토리북의 마지막(10번째) 장을 완료해 그 책이 완성됨 (진한 주황)
 *    어느 마크인지는 서버의 recordedDays[].hasCompletedStorybook 이 결정
 */
enum class DayMark {
    PAGE_COMPLETED,
    STORYBOOK_COMPLETED
}

/**
 * 달력의 한 칸
 */
data class CalendarDay(
    val day: Int,
    val inCurrentMonth: Boolean = true,  // false = 요일 정렬용 빈칸
    val mark: DayMark? = null,           // 서비스 로고 마크
    val isToday: Boolean = false         // 오늘날짜
)

/**
 * 캘린더 메인 조회(GET /api/v1/calendar) 한 달치 결과
 *
 * 달력(그 달의 일수·1일 요일)은 클라이언트가 계산하고
 * 서버는 데이터(기록된 날·요약)를 넘김
 *
 * @param completedChapterCount 그 달에 완료한 장 수 → 상단 "N개의 장을 기록했어요"
 * @param recordedDays 기록이 있는 날 목록 → 달력 마크
 * @param summary 하단 "N월 한달 요약"
 */
data class CalendarMonth(
    val completedChapterCount: Int,
    val recordedDays: List<RecordedDay>,
    val summary: MonthSummary
)

/**
 * 기록이 있는 날 한 건
 *
 * @param hasCompletedStorybook 그 날 스토리북을 완성했는지 → 진한/연한 마크를 결정
 */
data class RecordedDay(
    val day: Int,
    val hasCompletedStorybook: Boolean
) {
    fun toDayMark(): DayMark =
        if (hasCompletedStorybook) DayMark.STORYBOOK_COMPLETED else DayMark.PAGE_COMPLETED
}

/**
 * 하단의 N월 한달 요약 카드 데이터
 */
data class MonthSummary(
    val completedCount: Int,                 // 완성 스토리북 개수
    val inProgressCount: Int,                // 진행 중 스토리북 개수
    val completedBooks: List<CompletedBook>  // 책꽂이에 꽂히는 완료 책(완료된 것만)
) {
    // 완료한 책이 하나도 없으면 '바로 시작하기' CTA 상태로 표시
    val isEmpty: Boolean get() = completedBooks.isEmpty()
}

/**
 * 책꽂이에 꽂히는 완료 스토리북(책등)
 *
 * 책등 그림은 앱 자산이라 storybookId 로 고름(테마 10종의 고정 ID)
 * 매핑표는 presentation 의 storybookSpineOf() 참고
 */
data class CompletedBook(
    val storybookId: Long
)
