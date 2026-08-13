package com.umc.halo.data.repository.storybook

import com.umc.halo.core.network.toApiErrorMessage
import com.umc.halo.data.remote.api.storybook.StorybookApi
import com.umc.halo.data.remote.dto.response.storybook.StorybookSummaryResponse
import com.umc.halo.domain.model.storybook.CustomStorybook
import com.umc.halo.domain.model.storybook.InProgressStorybook
import com.umc.halo.domain.model.storybook.Storybook
import com.umc.halo.domain.model.storybook.StorybookListResult
import com.umc.halo.domain.model.storybook.StorybookProgress
import com.umc.halo.domain.model.storybook.StorybookTheme
import com.umc.halo.domain.repository.storybook.StorybookRepository
import javax.inject.Inject

// 서버가 내려주는 진행 상태 (StorybookSummaryResponse.status)
private const val STATUS_IN_PROGRESS = "IN_PROGRESS"  // 진행중
private const val STATUS_TODAY_DONE = "TODAY_DONE"    // 진행중이지만 오늘 분량은 끝냄 → 내일까지 대기
private const val STATUS_COMPLETED = "COMPLETED"      // 10장까지 완료

/**
 * 스토리북 목록 화면 저장소 구현체
 */
class StorybookRepositoryImpl @Inject constructor(
    private val storybookApi: StorybookApi
) : StorybookRepository {

    override suspend fun getStorybookList(): StorybookListResult {
        val response = runCatching { storybookApi.getStorybooks() }
            .getOrElse { throwable ->
                throw IllegalStateException(throwable.toApiErrorMessage(LOAD_FAILED_MESSAGE))
            }

        val result = response.result ?: error(response.toApiErrorMessage(LOAD_FAILED_MESSAGE))

        val summaries = result.storybooks.orEmpty()
        val summaryById = summaries.associateBy { it.storybookId }

        return StorybookListResult(
            // 전체 탭 - 상황별 테마 섹션
            themes = result.situationalRecommendations.orEmpty().mapNotNull { section ->
                val sectionTitle = section.tag ?: return@mapNotNull null  // 태그를 식별자로도 사용
                StorybookTheme(
                    title = sectionTitle,
                    storybooks = section.storybooks.orEmpty().map { item ->
                        val summary = summaryById[item.storybookId]
                        Storybook(
                            id = item.storybookId,
                            title = item.title ?: summary?.title.orEmpty(),
                            subtitle = summary?.shortDescription.orEmpty(),
                            imageUrl = item.imageUrl ?: summary?.imageUrl,
                            progress = summary?.toProgress()
                        )
                    }
                )
            },

            // 진행중 탭 - 오늘 분량을 끝낸 책(TODAY_DONE)도 아직 진행중이므로 함께 담고 대기 상태로 표시
            // 먼저 시작한 책부터 좌상단에 (날짜가 "yyyy-MM-dd" 라 문자열 오름차순 = 시간순)
            inProgressStorybooks = summaries
                .filter { it.status == STATUS_IN_PROGRESS || it.status == STATUS_TODAY_DONE }
                .sortedBy { it.startedDate ?: LAST_DATE }  // 시작일이 없으면 맨 뒤로
                .map { summary ->
                    InProgressStorybook(
                        id = summary.storybookId,
                        title = summary.title.orEmpty(),
                        subtitle = summary.shortDescription.orEmpty(),
                        imageUrl = summary.imageUrl,
                        currentChapter = summary.lastChapterOrder ?: 0,
                        isWaiting = summary.status == STATUS_TODAY_DONE
                    )
                },

            // 완료 탭 - 가장 최근에 끝낸 책부터 좌상단에 (문자열 내림차순 = 최신순)
            doneStorybooks = summaries
                .filter { it.status == STATUS_COMPLETED }
                .sortedByDescending { it.lastCompletedDate ?: FIRST_DATE }  // 기록일이 없으면 맨 뒤로
                .map { summary ->
                    Storybook(
                        id = summary.storybookId,
                        title = summary.title.orEmpty(),
                        subtitle = summary.shortDescription.orEmpty(),
                        imageUrl = summary.imageUrl,
                        progress = StorybookProgress.Done
                    )
                }
        )
    }

    override suspend fun getRecommendedStorybooks(): List<CustomStorybook> {
        val response = runCatching { storybookApi.getRecommendedStorybooks() }
            .getOrElse { throwable ->
                throw IllegalStateException(throwable.toApiErrorMessage(RECOMMENDED_FAILED_MESSAGE))
            }

        val result = response.result
            ?: error(response.toApiErrorMessage(RECOMMENDED_FAILED_MESSAGE))

        return result.storybooks.orEmpty().map {
            CustomStorybook(
                id = it.storybookId,
                tag = it.recommendationReasonText.orEmpty(),
                title = it.title.orEmpty(),
                subtitle = it.shortDescription.orEmpty(),
                imageUrl = it.imageUrl.orEmpty()
            )
        }
    }

    /**
     * 카드 배지(책갈피)에 쓰는 진행 상태
     *
     * NOT_STARTED는 애초에 제외
     *
     * "N장 진행중" 의 N = [StorybookSummaryResponse.lastChapterOrder]
     */
    private fun StorybookSummaryResponse.toProgress(): StorybookProgress? = when (status) {
        STATUS_IN_PROGRESS, STATUS_TODAY_DONE -> StorybookProgress.InProgress(lastChapterOrder ?: 0)
        STATUS_COMPLETED -> StorybookProgress.Done
        else -> null
    }

    private companion object {
        // 정렬에서 '날짜 없음' 을 맨 뒤로 보내기 위한 값
        const val LAST_DATE = "9999-99-99"   // 오름차순(진행중 탭)용
        const val FIRST_DATE = "0000-00-00"  // 내림차순(완료 탭)용

        const val LOAD_FAILED_MESSAGE = "스토리북을 불러오지 못했어요. 잠시 후 다시 시도해 주세요."
        const val RECOMMENDED_FAILED_MESSAGE = "맞춤 스토리북을 불러오지 못했어요."
    }
}
