package com.umc.halo.presentation.mypage.relationship

data class RelationshipInfoUiState(
    val isLoading: Boolean = false,
    val parentPersonalityTags: List<String> = emptyList(),
    val currentRelationState: String? = null,
    val goalRelationships: List<String> = emptyList(),
    val errorMessage: String? = null
) {
    val parentPersonalityDisplayTags: List<String>
        get() = parentPersonalityTags.ifEmpty { listOf(FALLBACK_TEXT) }

    val currentRelationStateDisplayText: String
        get() = currentRelationState ?: FALLBACK_TEXT

    val goalRelationshipDisplayTags: List<String>
        get() = goalRelationships.ifEmpty { listOf(FALLBACK_TEXT) }

    companion object {
        private const val FALLBACK_TEXT = "-"
    }
}
