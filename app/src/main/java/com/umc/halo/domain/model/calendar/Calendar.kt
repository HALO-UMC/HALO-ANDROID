package com.umc.halo.domain.model.calendar

/**
 * 날짜 칸에 찍히는 서비스 로고 종류
 *  - [PAGE_COMPLETED]      : 그 날 어떤 스토리북의 한 장(챕터)을 완료 (연한 주황)
 *  - [STORYBOOK_COMPLETED] : 그 날 어떤 스토리북의 마지막(10번째) 장을 완료해 그 책이 완성됨 (진한 주황)
 *    ※ 스토리북당 하루 1장이라 "하루에 10장 전부" 가 아니라 '마지막 장을 끝낸 날' 을 뜻함
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
 * 커버 이미지/테마 고유색은 서버 연동 후 확정 — 지금은 더미 데이터로
 */
data class CompletedBook(
    val id: Long,
    val title: String
)
