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

    private fun updateName(name: String) {
        val filteredName = name
            .filter { character ->
                character.toString().matches(NAME_ALLOWED_REGEX)
            }
            .take(MAX_NAME_LENGTH)

        updateState {
            copy(name = filteredName)
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

                selectedParentPersonalities.size < MAX_PARENT_PERSONALITY_COUNT -> {
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
        private const val MAX_NAME_LENGTH = 10
        private const val MAX_PARENT_PERSONALITY_COUNT = 3

        // 한글 완성형, 한글 조합 중간 글자, 영어, 숫자 허용
        private val NAME_ALLOWED_REGEX = Regex("[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9]")
    }
}