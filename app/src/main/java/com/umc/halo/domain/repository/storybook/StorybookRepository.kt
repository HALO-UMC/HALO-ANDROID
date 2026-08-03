package com.umc.halo.domain.repository.storybook

import com.umc.halo.domain.model.storybook.CustomStorybook
import com.umc.halo.domain.model.storybook.StorybookListResult

/**
 * 스토리북 목록 화면 저장소 인터페이스
 */
interface StorybookRepository {

    /** 전체/진행중/완료 3탭에 필요한 목록을 한 번에 받아옴 */
    suspend fun getStorybookList(): StorybookListResult

    /** 전체 탭 상단 "OO님 맞춤 스토리북" 2권 */
    suspend fun getRecommendedStorybooks(): List<CustomStorybook>
}
