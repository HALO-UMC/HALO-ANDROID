package com.umc.halo.presentation.onboarding

internal const val MIN_NAME_LENGTH = 2
internal const val MAX_NAME_LENGTH = 10

private const val MAX_PARENT_PERSONALITY_COUNT = 3

private const val NAME_VALIDATION_MESSAGE =
    "2~10자 이내의 한글/영어/숫자로 입력해주세요."

// 완성형 한글, 한글 조합 중간 자음·모음, 영어, 숫자 허용
private val NAME_ALLOWED_CHARACTER_REGEX = Regex(
    "[가-힣ㄱ-ㅎㅏ-ㅣ\u1100-\u11FF\uA960-\uA97F\uD7B0-\uD7FFa-zA-Z0-9]"
)

internal fun Char.isAllowedNameCharacter(): Boolean {
    return NAME_ALLOWED_CHARACTER_REGEX.matches(toString())
}

enum class Gender(
    val label: String
) {
    MALE("남자"),
    FEMALE("여자")
}

data class OnboardingUiState(
    val currentStep: OnboardingStep = OnboardingStep.NAME,

    val name: String = "",

    // 특수문자 입력, 글자 수 초과 등의 잘못된 입력 시도 여부
    val isNameErrorVisible: Boolean = false,

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
        get() = name.isNotBlank() &&
                name.all { character ->
                    character.isAllowedNameCharacter()
                }

    val isNameValid: Boolean
        get() = isNameLengthValid && isNameFormatValid

    val nameErrorMessage: String?
        get() = if (isNameErrorVisible) {
            NAME_VALIDATION_MESSAGE
        } else {
            null
        }

    val isBirthDateSelected: Boolean
        get() = birthYear != null &&
                birthMonth != null &&
                birthDay != null

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
        get() = selectedParentPersonalities.size in
                1..MAX_PARENT_PERSONALITY_COUNT

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