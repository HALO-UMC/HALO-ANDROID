package com.umc.halo.domain.repository.storybook

interface StorybookDetailRepository {
    suspend fun getStorybookDetail(storybookId: Long)
}