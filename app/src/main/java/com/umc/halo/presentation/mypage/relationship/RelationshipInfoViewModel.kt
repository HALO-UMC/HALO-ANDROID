package com.umc.halo.presentation.mypage.relationship

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.halo.domain.repository.relationship.RelationshipRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RelationshipInfoViewModel @Inject constructor(
    private val relationshipRepository: RelationshipRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RelationshipInfoUiState())
    val uiState: StateFlow<RelationshipInfoUiState> = _uiState.asStateFlow()

    init {
        loadRelationshipInfo()
    }

    fun errorMessageShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun loadRelationshipInfo() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            runCatching { relationshipRepository.getRelationshipInfo() }
                .onSuccess { info ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            parentPersonalityTags = info.parentPersonalityTags.map { tag -> tag.title },
                            currentRelationState = info.currentRelationState?.title,
                            goalRelationships = info.goalRelationships.map { tag -> tag.title }
                        )
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            parentPersonalityTags = emptyList(),
                            currentRelationState = null,
                            goalRelationships = emptyList(),
                            errorMessage = LOAD_FAILED_MESSAGE
                        )
                    }
                }
        }
    }

    private companion object {
        const val LOAD_FAILED_MESSAGE = "관계 정보를 불러오지 못했어요."
    }
}
