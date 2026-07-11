package com.umc.halo.domain.model.home

sealed interface UserState {
    data object FTU: UserState
    data class RU(
        val currentProgress: CurrentProgress,
        val progressState: ProgressState
    ): UserState
}

enum class ProgressState {
    InProgress,
    Complete,
    BeforeStart
}

data class CurrentProgress(
    val theme: Int,
    val chapter: Int
)