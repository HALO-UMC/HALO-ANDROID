package com.umc.halo.domain.repository.storybook

import com.umc.halo.domain.model.storybook.StorybookDetailResult

interface StorybookDetailRepository {
    suspend fun getStorybookDetail(storybookId: Long): StorybookDetailResult
}