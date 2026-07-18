package com.umc.halo.presentation.onboarding

import com.umc.halo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor() :
    BaseViewModel<OnboardingUiState, OnboardingUiEvent>(
        initialState = OnboardingUiState()
    ) {

    override fun onEvent(event: OnboardingUiEvent) {
        when (event) {
            is OnboardingUiEvent.NameChanged -> {
                updateName(event.name)
            }

            is OnboardingUiEvent.GenderSelected -> {
                updateGender(event.gender)
            }

            is OnboardingUiEvent.BirthYearSelected -> {
                updateBirthYear(event.year)
            }

            is OnboardingUiEvent.BirthMonthSelected -> {
                updateBirthMonth(event.month)
            }

            is OnboardingUiEvent.BirthDaySelected -> {
                updateBirthDay(event.day)
            }

            is OnboardingUiEvent.ParentPersonalityClicked -> {
                toggleParentPersonality(event.personality)
            }

            is OnboardingUiEvent.RelationshipClicked -> {
                updateRelationship(event.relationship)
            }

            is OnboardingUiEvent.GoalClicked -> {
                updateGoal(event.goal)
            }

            OnboardingUiEvent.NextClicked -> {
                moveToNextStep()
            }

            OnboardingUiEvent.BackClicked -> {
                moveToPreviousStep()
            }
        }
    }

    private fun updateName(input: String) {
        val containsInvalidCharacter = input.any { character ->
            !character.isAllowedNameCharacter()
        }

        val exceedsMaxLength = input.length > MAX_NAME_LENGTH

        /*
         * 허용되지 않은 문자는 실제 입력값에는 반영하지 않는다.
         * 10자를 초과한 문자도 입력값에는 반영하지 않는다.
         */
        val filteredName = input
            .filter { character ->
                character.isAllowedNameCharacter()
            }
            .take(MAX_NAME_LENGTH)

        val shouldShowError = when {
            // 아무것도 입력하지 않은 초기 상태에서는 에러를 보여주지 않는다.
            input.isEmpty() -> false

            // 특수문자를 입력했거나 10자를 초과하려 한 경우
            containsInvalidCharacter -> true
            exceedsMaxLength -> true

            // 허용된 문자이지만 아직 2자 미만인 경우
            filteredName.length < MIN_NAME_LENGTH -> true

            else -> false
        }

        updateState {
            copy(
                name = filteredName,
                isNameErrorVisible = shouldShowError
            )
        }
    }

    private fun updateGender(gender: Gender) {
        updateState {
            copy(selectedGender = gender)
        }
    }

    private fun updateBirthYear(year: Int) {
        updateState {
            copy(birthYear = year)
        }
    }

    private fun updateBirthMonth(month: Int) {
        updateState {
            copy(birthMonth = month)
        }
    }

    private fun updateBirthDay(day: Int) {
        updateState {
            copy(birthDay = day)
        }
    }

    private fun toggleParentPersonality(personality: String) {
        updateState {
            val newList = when {
                personality in selectedParentPersonalities -> {
                    selectedParentPersonalities - personality
                }

                selectedParentPersonalities.size <
                        MAX_PARENT_PERSONALITY_COUNT -> {
                    selectedParentPersonalities + personality
                }

                else -> {
                    selectedParentPersonalities
                }
            }

            copy(selectedParentPersonalities = newList)
        }
    }

    private fun updateRelationship(relationship: String) {
        updateState {
            copy(selectedRelationship = relationship)
        }
    }

    private fun updateGoal(goal: String) {
        updateState {
            copy(selectedGoal = goal)
        }
    }

    private fun moveToNextStep() {
        updateState {
            if (!isNextEnabled) {
                this
            } else {
                copy(currentStep = currentStep.next())
            }
        }
    }

    private fun moveToPreviousStep() {
        updateState {
            copy(currentStep = currentStep.previous())
        }
    }

    companion object {
        private const val MAX_PARENT_PERSONALITY_COUNT = 3
    }
}