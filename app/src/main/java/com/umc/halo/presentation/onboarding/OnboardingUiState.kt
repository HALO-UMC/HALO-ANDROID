package com.umc.halo.presentation.onboarding

enum class Gender {
    FEMALE,
    MALE
}

data class OnboardingUiState(
    val currentStep: OnboardingStep = OnboardingStep.NAME,

    val name: String = "",

    val selectedGender: Gender? = null,
    val birthYear: String = "",
    val birthMonth: String = "",
    val birthDay: String = "",

    val selectedParentPersonalities: List<String> = emptyList(),
    val selectedRelationship: String? = null,
    val selectedGoal: String? = null
) {
    val userName: String
        get() = name.ifBlank { "주연" }

    val isNextEnabled: Boolean
        get() = when (currentStep) {
            OnboardingStep.NAME -> {
                name.isNotBlank()
            }

            OnboardingStep.BASIC_INFO -> {
                selectedGender != null &&
                        birthYear.length == 4 &&
                        birthMonth.isNotBlank() &&
                        birthDay.isNotBlank()
            }

            OnboardingStep.WELCOME -> {
                true
            }

            OnboardingStep.PARENT_PERSONALITY -> {
                selectedParentPersonalities.isNotEmpty()
            }

            OnboardingStep.RELATIONSHIP -> {
                selectedRelationship != null
            }

            OnboardingStep.GOAL -> {
                selectedGoal != null
            }

            OnboardingStep.COMPLETE -> {
                true
            }
        }
}