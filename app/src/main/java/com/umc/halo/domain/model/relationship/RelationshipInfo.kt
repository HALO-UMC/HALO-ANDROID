package com.umc.halo.domain.model.relationship

data class RelationshipInfo(
    val parentPersonalityTags: List<RelationshipTag>,
    val currentRelationState: RelationshipTag?,
    val goalRelationships: List<RelationshipTag>
)
