package com.umc.halo.data.repository.calendar

import com.umc.halo.core.network.BaseResponse
import com.umc.halo.core.network.toApiErrorMessage
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
        val response = runCatching {
            calendarApi.getCalendarMonth(year = year, month = month)
        }.getOrElse { throwable ->
            throw IllegalStateException(
                throwable.toApiErrorMessage("기록을 불러오지 못했어요. 잠시 후 다시 시도해 주세요.")
            )
        }
        val result = response.result
        if (!response.isSuccess || result == null) {
            error(response.toApiErrorMessage("기록을 불러오지 못했어요. 잠시 후 다시 시도해 주세요."))
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
                // 책등 그림은 앱 자산이라 서버는 id 만 주면 되고 매핑은 화면이 함
                completedBooks = result.completedStorybooks.orEmpty().map { dto ->
                    CompletedBook(storybookId = dto.storybookId)
                }
            )
        )
    }

    override suspend fun getDayRecord(year: Int, month: Int, day: Int): DayRecord {
        val response = runCatching {
            calendarApi.getDayRecord(date = formatDate(year, month, day))
        }.getOrElse { throwable ->
            throw IllegalStateException(
                throwable.toApiErrorMessage("그 날의 기록을 불러오지 못했어요. 잠시 후 다시 시도해 주세요.")
            )
        }
        val result = response.result
        if (!response.isSuccess || result == null) {
            error(response.toApiErrorMessage("그 날의 기록을 불러오지 못했어요. 잠시 후 다시 시도해 주세요."))
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
                    memberChapterId = dto.memberChapterId,
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

