package com.umc.halo.domain.repository.calendar

import com.umc.halo.domain.model.calendar.CalendarMonth
import com.umc.halo.domain.model.calendar.DayRecord

/**
 * 캘린더 데이터 조회
 */
interface CalendarRepository {

    /** 월별 현황 (달력 마크 + 상단/하단 요약) */
    suspend fun getMonth(year: Int, month: Int): CalendarMonth

    /** 특정 날짜의 기록 (날짜 클릭 모달) */
    suspend fun getDayRecord(year: Int, month: Int, day: Int): DayRecord
}
