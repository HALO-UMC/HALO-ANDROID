package com.umc.halo.data.repository.storybook

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
        val result = storybookApi.getStorybooks().result
            ?: throw IllegalStateException("storybook list data is null")

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
            // TODO: 디자인은 '시작일이 빠른 순' 인데 서버가 시작일을 주지 않아 지금은 서버 순서(테마 순서) 그대로 씀
            inProgressStorybooks = summaries
                .filter { it.status == STATUS_IN_PROGRESS || it.status == STATUS_TODAY_DONE }
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

            // 완료 탭 - 완료가 빠른 순으로 좌상단부터 (날짜가 "yyyy-MM-dd" 라 문자열 정렬로 시간순이 됨)
            doneStorybooks = summaries
                .filter { it.status == STATUS_COMPLETED }
                .sortedBy { it.lastCompletedDate ?: LAST_DATE }  // 날짜가 없으면 맨 뒤로
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
        val result = storybookApi.getRecommendedStorybooks().result
            ?: throw IllegalStateException("recommended storybook data is null")

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
     * TODO: "N장 진행중" 의 N 을 '완료한 장 수(lastChapterOrder)' 로 지금 서버에서 내려주어
     *  '지금 진행중인 장' 은 주지 않음 -> 확인 필요
     */
    private fun StorybookSummaryResponse.toProgress(): StorybookProgress? = when (status) {
        STATUS_IN_PROGRESS, STATUS_TODAY_DONE -> StorybookProgress.InProgress(lastChapterOrder ?: 0)
        STATUS_COMPLETED -> StorybookProgress.Done
        else -> null
    }

    private companion object {
        // 정렬에서 '날짜 없음' 을 맨 뒤로 보내기 위한 값
        const val LAST_DATE = "9999-99-99"
    }
}
