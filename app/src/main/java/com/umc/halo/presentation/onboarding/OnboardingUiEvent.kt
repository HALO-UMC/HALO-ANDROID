package com.umc.halo.presentation.onboarding

sealed interface OnboardingUiEvent {
    data class NameChanged(val name: String) : OnboardingUiEvent

    data class GenderSelected(val gender: Gender) : OnboardingUiEvent
    data class BirthYearChanged(val year: String) : OnboardingUiEvent
    data class BirthMonthChanged(val month: String) : OnboardingUiEvent
    data class BirthDayChanged(val day: String) : OnboardingUiEvent

    data class ParentPersonalityClicked(val personality: String) : OnboardingUiEvent
    data class RelationshipClicked(val relationship: String) : OnboardingUiEvent
    data class GoalClicked(val goal: String) : OnboardingUiEvent

    data object NextClicked : OnboardingUiEvent
    data object BackClicked : OnboardingUiEvent
}