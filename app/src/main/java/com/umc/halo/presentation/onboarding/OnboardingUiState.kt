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

private fun isValidBirthDate(
    year: Int,
    month: Int,
    day: Int
): Boolean {
    if (year <= 0) return false
    if (month !in 1..12) return false

    val maxDay = when (month) {
        1, 3, 5, 7, 8, 10, 12 -> {
            31
        }

        4, 6, 9, 11 -> {
            30
        }

        2 -> {
            val isLeapYear =
                year % 400 == 0 ||
                        (year % 4 == 0 && year % 100 != 0)

            if (isLeapYear) 29 else 28
        }

        else -> {
            return false
        }
    }

    return day in 1..maxDay
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

    /*
     * 기존 화면 코드에서 사용하는 이름을 유지한다.
     * 세 값이 단순히 선택됐는지가 아니라 실제 존재하는 날짜인지도 검사한다.
     */
    val isBirthDateSelected: Boolean
        get() = isBirthDateValid

    val birthDateText: String
        get() {
            if (!isBirthDateValid) return ""

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
                /*
                 * 잘못된 문자를 제거한 뒤 저장된 이름이 유효하더라도
                 * 현재 오류가 표시 중이면 다음 단계로 이동하지 못하게 한다.
                 */
                isNameValid && !isNameErrorVisible
            }

            OnboardingStep.BASIC_INFO -> {
                selectedGender != null && isBirthDateValid
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