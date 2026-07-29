package com.umc.halo.domain.model.home

/**
 * 사용자의 유형 (FTU: 앱을 처음 이용하는 사용자, RU: 기존 사용자)
 */
sealed interface UserState {
    data object FTU: UserState
    data object RU: UserState
}