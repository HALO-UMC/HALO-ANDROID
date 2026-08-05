package com.umc.halo.data.remote.dto.response.relationship

data class RelationshipInfoResponse(
    val parentPersonalityTags: List<RelationshipTagResponse>?,
    val currentRelationState: RelationshipTagResponse?,
    val goalRelationships: List<RelationshipTagResponse>?
)

data class RelationshipTagResponse(
    val tagId: Long?,
    val title: String?,
    val description: String? = null
)
