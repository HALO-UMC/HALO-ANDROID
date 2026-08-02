package com.umc.halo.data.repository.calendar

import com.umc.halo.data.remote.api.calendar.CalendarApi
import com.umc.halo.domain.model.calendar.CalendarMonth
import com.umc.halo.domain.model.calendar.CompletedBook
import com.umc.halo.domain.model.calendar.DateCompletedChapter
import com.umc.halo.domain.model.calendar.DateCompletedStorybook
import com.umc.halo.domain.model.calendar.DayRecord
import com.umc.halo.domain.model.calendar.MonthSummary
import com.umc.halo.domain.model.calendar.RecordedDay
import com.umc.halo.domain.repository.calendar.CalendarRepository
import java.util.Locale
import javax.inject.Inject

/**
 * CalendarRepository 구현체
 * 서버 호출(CalendarApi) → DTO 를 도메인 모델로 변환
 */
class CalendarRepositoryImpl @Inject constructor(
    private val calendarApi: CalendarApi
) : CalendarRepository {

    override suspend fun getMonth(year: Int, month: Int): CalendarMonth {
        val response = calendarApi.getCalendarMonth(year = year, month = month)
        val result = response.result
        if (!response.isSuccess || result == null) {
            error("캘린더 메인 조회 실패 (code=${response.code}, message=${response.message})")
        }

        val stats = result.stats
        return CalendarMonth(
            completedChapterCount = stats?.completedPageCount ?: 0,
            recordedDays = result.recordedDays.orEmpty().map { dto ->
                RecordedDay(
                    day = dto.day,
                    hasCompletedStorybook = dto.hasCompletedStorybook
                )
            },
            summary = MonthSummary(
                completedCount = stats?.completedStorybookCount ?: 0,
                inProgressCount = stats?.inProgressStorybookCount ?: 0,
                // 서버가 id 만 주므로 책등 이미지는 화면에서 순서대로 배정
                // TODO : 백엔드와 협의
                completedBooks = result.completedStorybooks.orEmpty().map { dto ->
                    CompletedBook(storybookId = dto.storybookId)
                }
            )
        )
    }

    override suspend fun getDayRecord(year: Int, month: Int, day: Int): DayRecord {
        val response = calendarApi.getDayRecord(date = formatDate(year, month, day))
        val result = response.result
        if (!response.isSuccess || result == null) {
            error("일별 기록 조회 실패 (code=${response.code}, message=${response.message})")
        }

        return DayRecord(
            month = month,
            day = day,
            completedStorybooks = result.storybooks.orEmpty().map { dto ->
                DateCompletedStorybook(
                    storybookId = dto.storybookId,
                    title = dto.title,
                    imageUrl = dto.storybookImageUrl
                )
            },
            completedChapters = result.chapters.orEmpty().map { dto ->
                DateCompletedChapter(
                    storybookId = dto.storybookId,
                    title = dto.title,
                    chapterOrder = dto.completedChapterOrder
                )
            }
        )
    }

    /**
     * yyyy-MM-dd 로 변환
     */
    private fun formatDate(year: Int, month: Int, day: Int): String =
        String.format(Locale.US, "%04d-%02d-%02d", year, month, day)
}
