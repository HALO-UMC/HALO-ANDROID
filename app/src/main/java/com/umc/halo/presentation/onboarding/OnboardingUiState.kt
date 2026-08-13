package com.umc.halo.presentation.onboarding

import com.umc.halo.domain.model.onboarding.OnboardingTag
internal const val MIN_NAME_LENGTH = 2
internal const val MAX_NAME_LENGTH = 10
internal const val MAX_PARENT_PERSONALITY_COUNT = 3
internal const val MAX_GOAL_COUNT = 2

internal const val NAME_VALIDATION_MESSAGE =
    "이름 조건에 맞춰 다시 입력해주세요!"
internal const val DUPLICATE_NICKNAME_MESSAGE =
    "중복된 닉네임 입니다!"
internal const val NICKNAME_CHECK_FAILED_MESSAGE =
    "이름 확인에 실패했어요. 잠시 후 다시 시도해주세요."
internal const val TAG_LOAD_FAILED_MESSAGE =
    "선택지를 불러오지 못했어요. 잠시 후 다시 시도해주세요."
internal const val SAVE_FAILED_MESSAGE =
    "저장에 실패했어요. 잠시 후 다시 시도해주세요."
private const val GOAL_LIMIT_MESSAGE =
    "2개까지 선택 가능해요."

private val NAME_ALLOWED_CHARACTER_REGEX = Regex(
    "[가-힣ㄱ-ㅎㅏ-ㅣ\\u1100-\\u11FF\\uA960-\\uA97F\\uD7B0-\\uD7FFa-zA-Z0-9]"
)

internal fun Char.isAllowedNameCharacter(): Boolean {
    return NAME_ALLOWED_CHARACTER_REGEX.matches(toString())
}

private fun isValidBirthDate(
    year: Int,
    month: Int,
    day: Int
): Boolean {
    if (year <= 0) return false
    if (month !in 1..12) return false

    val maxDay = when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> {
            val isLeapYear =
                year % 400 == 0 ||
                        (year % 4 == 0 && year % 100 != 0)

            if (isLeapYear) 29 else 28
        }

        else -> return false
    }

    if (day !in 1..maxDay) return false

    val today = java.util.Calendar.getInstance()
    val selectedDate = java.util.Calendar.getInstance().apply {
        clear()
        set(year, month - 1, day)
    }

    return !selectedDate.after(today)
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
    val nameErrorText: String? = null,

    val selectedGender: Gender? = null,
    val birthYear: Int? = null,
    val birthMonth: Int? = null,
    val birthDay: Int? = null,
    val hasBirthDateTouched: Boolean = false,

    val parentPersonalityTags: List<OnboardingTag> = emptyList(),
    val currentRelationStateTags: List<OnboardingTag> = emptyList(),
    val goalRelationshipTags: List<OnboardingTag> = emptyList(),

    val selectedParentPersonalities: List<OnboardingTag> = emptyList(),
    val selectedRelationship: OnboardingTag? = null,
    val selectedGoals: List<OnboardingTag> = emptyList(),

    val isGoalLimitMessageVisible: Boolean = false,
    val isInitialLoading: Boolean = false,
    val isSaving: Boolean = false,
    val loadErrorMessage: String? = null,
    val saveErrorMessage: String? = null,
    val navigateToHome: Boolean = false
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
        get() = nameErrorText

    val isBirthDateValid: Boolean
        get() {
            val year = birthYear ?: return false
            val month = birthMonth ?: return false
            val day = birthDay ?: return false

            return isValidBirthDate(
                year = year,
                month = month,
                day = day
            )
        }

    val isBirthDateSelected: Boolean
        get() = hasBirthDateTouched && isBirthDateValid

    val birthDateText: String
        get() {
            if (!isBirthDateValid) return ""

            return "%04d.%02d.%02d".format(
                birthYear,
                birthMonth,
                birthDay
            )
        }

    val birthDateApiText: String
        get() {
            if (!isBirthDateValid) return ""

            return "%04d-%02d-%02d".format(
                birthYear,
                birthMonth,
                birthDay
            )
        }

    val isParentPersonalityValid: Boolean
        get() = selectedParentPersonalities.size in
                1..MAX_PARENT_PERSONALITY_COUNT

    val isGoalValid: Boolean
        get() = selectedGoals.size in 1..MAX_GOAL_COUNT

    val goalLimitMessage: String?
        get() = if (isGoalLimitMessageVisible) {
            GOAL_LIMIT_MESSAGE
        } else {
            null
        }

    val stepErrorMessage: String?
        get() = saveErrorMessage ?: loadErrorMessage

    val isNextEnabled: Boolean
        get() {
            if (isSaving || isInitialLoading) return false

            return when (currentStep) {
                OnboardingStep.NAME -> isNameValid && nameErrorText == null
                OnboardingStep.BASIC_INFO -> {
                    selectedGender != null &&
                            hasBirthDateTouched &&
                            isBirthDateValid
                }

                OnboardingStep.WELCOME -> true
                OnboardingStep.PARENT_PERSONALITY -> {
                    parentPersonalityTags.isNotEmpty() &&
                            loadErrorMessage == null &&
                            isParentPersonalityValid
                }

                OnboardingStep.RELATIONSHIP -> {
                    currentRelationStateTags.isNotEmpty() &&
                            loadErrorMessage == null &&
                            selectedRelationship != null
                }

                OnboardingStep.GOAL -> {
                    goalRelationshipTags.isNotEmpty() &&
                            loadErrorMessage == null &&
                            isGoalValid
                }

                OnboardingStep.COMPLETE -> true
            }
        }
}
