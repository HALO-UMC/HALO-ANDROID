package com.umc.halo.domain.repository.storybook

import com.umc.halo.data.remote.dto.response.storybook.StorybookStartResponse
import com.umc.halo.domain.model.storybook.StorybookDetailResult
import com.umc.halo.domain.model.storybook.StorybookStartResult

interface StorybookDetailRepository {
    suspend fun getStorybookDetail(storybookId: Long): StorybookDetailResult

    suspend fun startStorybook(storybookId: Long): StorybookStartResult
}