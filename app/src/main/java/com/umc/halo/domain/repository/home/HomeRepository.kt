package com.umc.halo.domain.repository.home

import com.umc.halo.data.remote.dto.response.home.HomeResponse
import com.umc.halo.domain.model.home.HomeResult

interface HomeRepository {
    suspend fun getHome(): HomeResult
}