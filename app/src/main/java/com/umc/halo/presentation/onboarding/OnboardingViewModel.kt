package com.umc.halo.presentation.onboarding

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.umc.halo.core.datastore.DeviceUuidDataStore
import com.umc.halo.domain.model.onboarding.OnboardingSavedData
import com.umc.halo.domain.model.onboarding.OnboardingStatus
import com.umc.halo.domain.model.onboarding.OnboardingTag
import com.umc.halo.domain.model.onboarding.OnboardingTags
import com.umc.halo.domain.repository.notification.NotificationRepository
import com.umc.halo.domain.repository.onboarding.OnboardingRepository
import com.umc.halo.domain.repository.settings.SettingsRepository
import com.umc.halo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository,
    private val settingsRepository: SettingsRepository
) : BaseViewModel<OnboardingUiState, OnboardingUiEvent>(
    initialState = OnboardingUiState()
) {

    init {
        loadInitialData()
    }

    override fun onEvent(event: OnboardingUiEvent) {
        when (event) {
            is OnboardingUiEvent.NameChanged -> updateName(event.name)
            is OnboardingUiEvent.GenderSelected -> updateGender(event.gender)
            is OnboardingUiEvent.BirthYearSelected -> updateBirthYear(event.year)
            is OnboardingUiEvent.BirthMonthSelected -> updateBirthMonth(event.month)
            is OnboardingUiEvent.BirthDaySelected -> updateBirthDay(event.day)
            is OnboardingUiEvent.ParentPersonalityClicked -> toggleParentPersonality(event.tag)
            is OnboardingUiEvent.RelationshipClicked -> updateRelationship(event.tag)
            is OnboardingUiEvent.GoalClicked -> toggleGoal(event.tag)
            OnboardingUiEvent.NextClicked -> handleNextClicked()
            OnboardingUiEvent.BackClicked -> moveToPreviousStep()
            OnboardingUiEvent.HomeNavigationHandled -> updateState { copy(navigateToHome = false) }
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            updateState {
                copy(
                    isInitialLoading = true,
                    loadErrorMessage = null
                )
            }

            val tagsDeferred = async {
                runCatching { onboardingRepository.getTags() }
            }
            val statusDeferred = async {
                runCatching { onboardingRepository.getStatus() }
            }

            val tagsResult = tagsDeferred.await()
            val statusResult = statusDeferred.await()

            val tags = tagsResult.getOrNull()
            val status = statusResult.getOrNull()

            if (status?.onboardingCompleted == true) {
                updateState {
                    copy(
                        isInitialLoading = false,
                        navigateToHome = true
                    )
                }
                return@launch
            }

            updateState {
                val nextState = copy(
                    isInitialLoading = false,
                    parentPersonalityTags = tags?.parentPersonalityTags.orEmpty(),
                    currentRelationStateTags = tags?.currentRelationStateTags.orEmpty(),
                    goalRelationshipTags = tags?.goalRelationshipTags.orEmpty(),
                    loadErrorMessage = if (tags == null) {
                        tagsResult.exceptionOrNull().toUserMessage(TAG_LOAD_FAILED_MESSAGE)
                    } else {
                        null
                    }
                )

                if (status == null) {
                    nextState
                } else {
                    nextState.restore(status)
                }
            }
        }
    }

    private fun updateName(input: String) {
        updateState {
            copy(
                name = input,
                nameErrorText = null,
                saveErrorMessage = null
            )
        }
    }

    private fun updateGender(gender: Gender) {
        updateState {
            copy(
                selectedGender = gender,
                saveErrorMessage = null
            )
        }
    }

    private fun updateBirthYear(year: Int) {
        updateState {
            copy(
                birthYear = year,
                hasBirthDateTouched = true,
                saveErrorMessage = null
            )
        }
    }

    private fun updateBirthMonth(month: Int) {
        updateState {
            copy(
                birthMonth = month,
                hasBirthDateTouched = true,
                saveErrorMessage = null
            )
        }
    }

    private fun updateBirthDay(day: Int) {
        updateState {
            copy(
                birthDay = day,
                hasBirthDateTouched = true,
                saveErrorMessage = null
            )
        }
    }

    private fun toggleParentPersonality(tag: OnboardingTag) {
        updateState {
            val updatedPersonalities = when {
                tag in selectedParentPersonalities -> selectedParentPersonalities - tag
                selectedParentPersonalities.size < MAX_PARENT_PERSONALITY_COUNT ->
                    selectedParentPersonalities + tag

                else -> selectedParentPersonalities
            }

            copy(
                selectedParentPersonalities = updatedPersonalities,
                saveErrorMessage = null
            )
        }
    }

    private fun updateRelationship(tag: OnboardingTag) {
        updateState {
            copy(
                selectedRelationship = tag,
                saveErrorMessage = null
            )
        }
    }

    private fun toggleGoal(tag: OnboardingTag) {
        updateState {
            when {
                tag in selectedGoals -> {
                    copy(
                        selectedGoals = selectedGoals - tag,
                        isGoalLimitMessageVisible = false,
                        saveErrorMessage = null
                    )
                }

                selectedGoals.size < MAX_GOAL_COUNT -> {
                    copy(
                        selectedGoals = selectedGoals + tag,
                        isGoalLimitMessageVisible = false,
                        saveErrorMessage = null
                    )
                }

                else -> {
                    copy(
                        isGoalLimitMessageVisible = true
                    )
                }
            }
        }
    }

    private fun handleNextClicked() {
        if (currentState.isSaving) return

        when (currentState.currentStep) {
            OnboardingStep.NAME -> submitName()
            OnboardingStep.BASIC_INFO -> submitBasicInfo()
            OnboardingStep.WELCOME -> moveToNextStep()
            OnboardingStep.PARENT_PERSONALITY -> submitParentPersonality()
            OnboardingStep.RELATIONSHIP -> submitRelationship()
            OnboardingStep.GOAL -> submitGoal()
            OnboardingStep.COMPLETE -> Unit
        }
    }

    private fun submitName() {
        if (!currentState.isNameValid) {
            updateState { copy(nameErrorText = NAME_VALIDATION_MESSAGE) }
            return
        }

        viewModelScope.launch {
            updateState {
                copy(
                    isSaving = true,
                    nameErrorText = null,
                    saveErrorMessage = null
                )
            }

            val isAvailable = runCatching {
                onboardingRepository.checkNickname(currentState.name)
            }.getOrElse { throwable ->
                updateState {
                    copy(
                        isSaving = false,
                        nameErrorText = throwable.toUserMessage(NICKNAME_CHECK_FAILED_MESSAGE)
                    )
                }
                return@launch
            }

            if (!isAvailable) {
                updateState {
                    copy(
                        isSaving = false,
                        nameErrorText = DUPLICATE_NICKNAME_MESSAGE
                    )
                }
                return@launch
            }

            val saveResult = runCatching {
                onboardingRepository.saveStep1(currentState.name)
            }

            updateState {
                if (saveResult.isSuccess) {
                    copy(
                        isSaving = false,
                        currentStep = OnboardingStep.BASIC_INFO
                    )
                } else {
                    copy(
                        isSaving = false,
                        nameErrorText = saveResult.exceptionOrNull().toUserMessage(SAVE_FAILED_MESSAGE)
                    )
                }
            }
        }
    }

    private fun submitBasicInfo() {
        val gender = currentState.selectedGender ?: return
        val birthDate = currentState.birthDateApiText
        if (birthDate.isBlank()) return

        saveAndMoveNext {
            onboardingRepository.saveStep2(
                gender = gender.name,
                birthDate = birthDate
            )
        }
    }

    private fun submitParentPersonality() {
        val tagIds = currentState.selectedParentPersonalities.map { it.id }
        if (tagIds.isEmpty()) return

        saveAndMoveNext {
            onboardingRepository.saveStep3(tagIds)
        }
    }

    private fun submitRelationship() {
        val tagId = currentState.selectedRelationship?.id ?: return

        saveAndMoveNext {
            onboardingRepository.saveStep4(tagId)
        }
    }

    private fun submitGoal() {
        val tagIds = currentState.selectedGoals.map { it.id }
        if (tagIds.isEmpty()) return

        saveAndMoveNext {
            onboardingRepository.saveStep5(tagIds)
        }
    }

    private fun saveAndMoveNext(
        save: suspend () -> com.umc.halo.domain.model.onboarding.OnboardingSaveResult
    ) {
        viewModelScope.launch {
            updateState {
                copy(
                    isSaving = true,
                    saveErrorMessage = null
                )
            }

            val saveResult = runCatching { save() }
            val result = saveResult.getOrNull()

            updateState {
                if (result == null) {
                    copy(
                        isSaving = false,
                        saveErrorMessage = saveResult.exceptionOrNull().toUserMessage(SAVE_FAILED_MESSAGE)
                    )
                } else if (
                    currentStep == OnboardingStep.GOAL &&
                    result.onboardingCompleted
                ) {
                    copy(
                        isSaving = false,
                        currentStep = OnboardingStep.COMPLETE
                    )
                } else {
                    copy(
                        isSaving = false,
                        currentStep = currentStep.next()
                    )
                }
            }
        }
    }

    private fun moveToNextStep() {
        updateState {
            copy(
                currentStep = currentStep.next(),
                saveErrorMessage = null
            )
        }
    }

    private fun moveToPreviousStep() {
        updateState {
            copy(
                currentStep = currentStep.previous(),
                saveErrorMessage = null
            )
        }
    }

    fun offAllNotification() = viewModelScope.launch {
        val notificationSettings = settingsRepository.getNotificationSettings()
        val newSettings = notificationSettings.copy(
            isAllNotificationEnabled = false
        )

        settingsRepository.updateNotificationSettings(newSettings)
    }

    fun onAllNotification() = viewModelScope.launch {
        val notificationSettings = settingsRepository.getNotificationSettings()
        val newSettings = notificationSettings.copy(
            isAllNotificationEnabled = true
        )

        settingsRepository.updateNotificationSettings(newSettings)
    }
}

private fun Throwable?.toUserMessage(defaultMessage: String): String {
    val message = this?.message?.takeIf { it.isNotBlank() } ?: return defaultMessage
    return if (message.endsWith("failed")) defaultMessage else message
}

private fun OnboardingUiState.restore(status: OnboardingStatus): OnboardingUiState {
    val savedData = status.savedData ?: return copy(
        currentStep = OnboardingStep.fromSavedServerStep(status.currentStep)
    )

    return copy(
        currentStep = OnboardingStep.fromSavedServerStep(status.currentStep),
        name = savedData.name.orEmpty(),
        selectedGender = savedData.gender?.let { gender ->
            runCatching { Gender.valueOf(gender) }.getOrNull()
        },
        birthYear = savedData.birthDate?.substringBefore("-")?.toIntOrNull() ?: birthYear,
        birthMonth = savedData.birthDate
            ?.split("-")
            ?.getOrNull(1)
            ?.toIntOrNull()
            ?: birthMonth,
        birthDay = savedData.birthDate
            ?.split("-")
            ?.getOrNull(2)
            ?.toIntOrNull()
            ?: birthDay,
        hasBirthDateTouched = savedData.birthDate != null || hasBirthDateTouched,
        selectedParentPersonalities = parentPersonalityTags.findAllById(
            savedData.parentPersonalityTagIds
        ),
        selectedRelationship = savedData.currentRelationStateTagId?.let { tagId ->
            currentRelationStateTags.firstOrNull { it.id == tagId }
        },
        selectedGoals = goalRelationshipTags.findAllById(
            savedData.goalRelationshipTagIds
        )
    )
}

private fun List<OnboardingTag>.findAllById(ids: List<Long>): List<OnboardingTag> {
    return ids.mapNotNull { id ->
        firstOrNull { tag -> tag.id == id }
    }
}
