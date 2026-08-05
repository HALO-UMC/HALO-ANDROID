package com.umc.halo.presentation.mypage.relationship

data class RelationshipInfoUiState(
    val isLoading: Boolean = false,
    val parentPersonalityTags: List<String> = emptyList(),
    val currentRelationState: RelationshipDisplayItem? = null,
    val goalRelationships: List<RelationshipDisplayItem> = emptyList(),
    val errorMessage: String? = null
) {
    val parentPersonalityDisplayTags: List<String>
        get() = parentPersonalityTags.ifEmpty { listOf(FALLBACK_TEXT) }

    val currentRelationStateDisplayItem: RelationshipDisplayItem
        get() = currentRelationState ?: RelationshipDisplayItem(FALLBACK_TEXT)

    val goalRelationshipDisplayItems: List<RelationshipDisplayItem>
        get() = goalRelationships.ifEmpty { listOf(RelationshipDisplayItem(FALLBACK_TEXT)) }

    companion object {
        private const val FALLBACK_TEXT = "-"
    }
}

data class RelationshipDisplayItem(
    val title: String,
    val description: String? = null
)
