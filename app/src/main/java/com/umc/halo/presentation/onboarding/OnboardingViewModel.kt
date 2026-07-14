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

            is OnboardingUiEvent.BirthYearChanged -> {
                updateBirthYear(event.year)
            }

            is OnboardingUiEvent.BirthMonthChanged -> {
                updateBirthMonth(event.month)
            }

            is OnboardingUiEvent.BirthDayChanged -> {
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
        updateState {
            copy(name = name)
        }
    }

    private fun updateGender(gender: Gender) {
        updateState {
            copy(selectedGender = gender)
        }
    }

    private fun updateBirthYear(year: String) {
        val onlyDigits = year.filter { it.isDigit() }.take(4)

        updateState {
            copy(birthYear = onlyDigits)
        }
    }

    private fun updateBirthMonth(month: String) {
        val onlyDigits = month.filter { it.isDigit() }.take(2)

        updateState {
            copy(birthMonth = onlyDigits)
        }
    }

    private fun updateBirthDay(day: String) {
        val onlyDigits = day.filter { it.isDigit() }.take(2)

        updateState {
            copy(birthDay = onlyDigits)
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
        private const val MAX_PARENT_PERSONALITY_COUNT = 3
    }
}