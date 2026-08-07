package com.umc.halo.domain.repository.anniversary

import com.umc.halo.domain.model.anniversary.AnniversaryOverview
import com.umc.halo.domain.model.anniversary.AnniversarySaveForm

interface AnniversaryRepository {
    suspend fun getAnniversaries(): AnniversaryOverview
    suspend fun createAnniversary(form: AnniversarySaveForm): Long
    suspend fun updateAnniversary(anniversaryId: Long, form: AnniversarySaveForm): Long
    suspend fun deleteAnniversaries(anniversaryIds: List<Long>)
}
