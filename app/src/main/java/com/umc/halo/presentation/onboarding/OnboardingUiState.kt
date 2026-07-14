package com.umc.halo.presentation.onboarding

private const val MIN_NAME_LENGTH = 2
private const val MAX_NAME_LENGTH = 10
private const val MAX_PARENT_PERSONALITY_COUNT = 3

private val NAME_REGEX = Regex("^[가-힣a-zA-Z0-9]+$")

enum class Gender(
    val label: String
) {
    MALE("남자"),
    FEMALE("여자")
}

data class OnboardingUiState(
    val currentStep: OnboardingStep = OnboardingStep.NAME,

    val name: String = "",

    val selectedGender: Gender? = null,
    val birthYear: Int? = null,
    val birthMonth: Int? = null,
    val birthDay: Int? = null,

    val selectedParentPersonalities: List<String> = emptyList(),
    val selectedRelationship: String? = null,
    val selectedGoal: String? = null
) {
    val userName: String
        get() = name.ifBlank { "주연" }

    val isNameLengthValid: Boolean
        get() = name.length in MIN_NAME_LENGTH..MAX_NAME_LENGTH

    val isNameFormatValid: Boolean
        get() = name.isNotBlank() && NAME_REGEX.matches(name)

    val isNameValid: Boolean
        get() = isNameLengthValid && isNameFormatValid

    val nameErrorMessage: String?
        get() = when {
            name.isBlank() -> null
            name.length < MIN_NAME_LENGTH -> "이름은 2자 이상 입력해주세요."
            name.length > MAX_NAME_LENGTH -> "이름은 10자 이내로 입력해주세요."
            !NAME_REGEX.matches(name) -> "한글, 영어, 숫자만 입력할 수 있어요."
            else -> null
        }

    val isBirthDateSelected: Boolean
        get() = birthYear != null && birthMonth != null && birthDay != null

    val birthDateText: String
        get() {
            if (!isBirthDateSelected) return ""

            return "%04d.%02d.%02d".format(
                birthYear,
                birthMonth,
                birthDay
            )
        }

    val isParentPersonalityValid: Boolean
        get() = selectedParentPersonalities.size in 1..MAX_PARENT_PERSONALITY_COUNT

    val isNextEnabled: Boolean
        get() = when (currentStep) {
            OnboardingStep.NAME -> {
                isNameValid
            }

            OnboardingStep.BASIC_INFO -> {
                selectedGender != null && isBirthDateSelected
            }

            OnboardingStep.WELCOME -> {
                true
            }

            OnboardingStep.PARENT_PERSONALITY -> {
                isParentPersonalityValid
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