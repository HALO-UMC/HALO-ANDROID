package com.umc.halo.data.repository.relationship

import com.umc.halo.data.remote.api.relationship.RelationshipApi
import com.umc.halo.data.remote.dto.response.relationship.RelationshipTagResponse
import com.umc.halo.domain.model.relationship.RelationshipInfo
import com.umc.halo.domain.model.relationship.RelationshipTag
import com.umc.halo.domain.repository.relationship.RelationshipRepository
import javax.inject.Inject

class RelationshipRepositoryImpl @Inject constructor(
    private val relationshipApi: RelationshipApi
) : RelationshipRepository {

    override suspend fun getRelationshipInfo(): RelationshipInfo {
        val result = relationshipApi.getRelationshipInfo().result
            ?: throw IllegalStateException("relationship info data is null")

        return RelationshipInfo(
            parentPersonalityTags = result.parentPersonalityTags.orEmpty().mapNotNull { it.toDomain() },
            currentRelationState = result.currentRelationState?.toDomain(),
            goalRelationships = result.goalRelationships.orEmpty().mapNotNull { it.toDomain() }
        )
    }
}

private fun RelationshipTagResponse.toDomain(): RelationshipTag? {
    val id = tagId ?: return null
    val title = title?.takeIf { it.isNotBlank() } ?: return null

    return RelationshipTag(
        id = id,
        title = title,
        description = description?.takeIf { it.isNotBlank() }
    )
}
