package com.umc.halo.domain.repository.relationship

import com.umc.halo.domain.model.relationship.RelationshipInfo

interface RelationshipRepository {
    suspend fun getRelationshipInfo(): RelationshipInfo
}
