package com.umc.halo.domain.model.home

sealed interface UserState {
    data object FTU: UserState
    data object RU: UserState
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