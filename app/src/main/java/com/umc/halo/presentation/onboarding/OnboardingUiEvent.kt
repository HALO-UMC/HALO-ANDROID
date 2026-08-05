package com.umc.halo.presentation.onboarding

import com.umc.halo.domain.model.onboarding.OnboardingTag

sealed interface OnboardingUiEvent {
    data class NameChanged(val name: String) : OnboardingUiEvent

    data class GenderSelected(val gender: Gender) : OnboardingUiEvent

    data class BirthYearSelected(val year: Int) : OnboardingUiEvent
    data class BirthMonthSelected(val month: Int) : OnboardingUiEvent
    data class BirthDaySelected(val day: Int) : OnboardingUiEvent

    data class ParentPersonalityClicked(val tag: OnboardingTag) : OnboardingUiEvent
    data class RelationshipClicked(val tag: OnboardingTag) : OnboardingUiEvent
    data class GoalClicked(val tag: OnboardingTag) : OnboardingUiEvent

    data object NextClicked : OnboardingUiEvent
    data object BackClicked : OnboardingUiEvent
    data object HomeNavigationHandled : OnboardingUiEvent
}
