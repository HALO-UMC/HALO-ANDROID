package com.umc.halo.data.repository.storybook

import com.umc.halo.data.remote.api.storybook.StorybookDetailApi
import com.umc.halo.domain.repository.storybook.StorybookDetailRepository
import javax.inject.Inject

class StorybookDetailRepositoryImpl @Inject constructor(
    storybookDetailApi: StorybookDetailApi
): StorybookDetailRepository {
    override suspend fun getStorybookDetail(storybookId: Long) {
        TODO("Not yet implemented")
    }

}