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
                toggleGoal(event.goal)
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
        /*
         * 허용되지 않은 문자는 실제 입력값에는 반영하지 않는다.
         * 10자를 초과한 문자도 입력값에는 반영하지 않는다.
         */
        val filteredName = input
            .filter { character ->
                character.isAllowedNameCharacter()
            }
            .take(MAX_NAME_LENGTH)

        updateState {
            copy(
                name = filteredName,
                isNameErrorVisible = false
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
            copy(
                birthYear = year,
                hasBirthDateTouched = true
            )
        }
    }

    private fun updateBirthMonth(month: Int) {
        updateState {
            copy(
                birthMonth = month,
                hasBirthDateTouched = true
            )
        }
    }

    private fun updateBirthDay(day: Int) {
        updateState {
            copy(
                birthDay = day,
                hasBirthDateTouched = true
            )
        }
    }

    private fun toggleParentPersonality(personality: String) {
        updateState {
            val updatedPersonalities = when {
                /*
                 * 이미 선택된 태그를 다시 누르면 선택 해제
                 */
                personality in selectedParentPersonalities -> {
                    selectedParentPersonalities - personality
                }

                /*
                 * 전체 선택 개수가 3개 미만이면 선택 추가
                 */
                selectedParentPersonalities.size <
                        MAX_PARENT_PERSONALITY_COUNT -> {
                    selectedParentPersonalities + personality
                }

                /*
                 * 이미 전체에서 3개를 선택했다면 변경하지 않음
                 */
                else -> {
                    selectedParentPersonalities
                }
            }

            copy(
                selectedParentPersonalities = updatedPersonalities
            )
        }
    }

    private fun updateRelationship(relationship: String) {
        updateState {
            copy(selectedRelationship = relationship)
        }
    }

    /*
     * 원하는 관계는 전체 항목 중 최소 1개, 최대 2개까지 선택한다.
     */
    private fun toggleGoal(goal: String) {
        updateState {
            when {
                /*
                 * 이미 선택된 항목을 다시 누르면 선택을 해제한다.
                 * 선택 상태가 정상적으로 변경됐으므로 안내 문구도 제거한다.
                 */
                goal in selectedGoals -> {
                    copy(
                        selectedGoals = selectedGoals - goal,
                        isGoalLimitMessageVisible = false
                    )
                }

                /*
                 * 현재 선택 개수가 두 개 미만이면 새 항목을 추가한다.
                 */
                selectedGoals.size < MAX_GOAL_COUNT -> {
                    copy(
                        selectedGoals = selectedGoals + goal,
                        isGoalLimitMessageVisible = false
                    )
                }

                /*
                 * 이미 두 개를 선택한 상태에서 세 번째 항목을 누른 경우다.
                 *
                 * 기존 선택 상태는 그대로 유지하고,
                 * 최대 선택 개수 안내 문구만 표시한다.
                 */
                else -> {
                    copy(
                        isGoalLimitMessageVisible = true
                    )
                }
            }
        }
    }

    private fun moveToNextStep() {
        updateState {
            when {
                currentStep == OnboardingStep.NAME && !isNameValid -> {
                    copy(isNameErrorVisible = true)
                }

                !isNextEnabled -> {
                    this
                }

                else -> {
                    copy(currentStep = currentStep.next())
                }
            }
        }
    }

    private fun moveToPreviousStep() {
        updateState {
            copy(currentStep = currentStep.previous())
        }
    }
}
