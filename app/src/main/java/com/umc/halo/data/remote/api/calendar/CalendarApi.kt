package com.umc.halo.data.remote.api.calendar

import com.umc.halo.core.network.BaseResponse
import com.umc.halo.data.remote.dto.response.calendar.CalendarMonthResponse
import com.umc.halo.data.remote.dto.response.calendar.DayRecordResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 캘린더 관련 서버 API
 */
interface CalendarApi {

    /** 월별 현황 — 달력 마크 + 상단/하단 요약 */
    @GET("api/v1/calendar")
    suspend fun getCalendarMonth(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): BaseResponse<CalendarMonthResponse>

    /**
     * 날짜별 기록 — 모달 내용
     *
     * @param date yyyy-MM-dd
     */
    @GET("api/v1/calendar/{date}")
    suspend fun getDayRecord(
        @Path("date") date: String
    ): BaseResponse<DayRecordResponse>
}
