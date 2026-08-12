package com.umc.halo.presentation.mypage.anniversary

import androidx.lifecycle.viewModelScope
import com.umc.halo.domain.model.anniversary.AnniversaryOverview
import com.umc.halo.domain.model.anniversary.AnniversarySaveForm
import com.umc.halo.domain.model.anniversary.CommonAnniversary
import com.umc.halo.domain.model.anniversary.MyAnniversary
import com.umc.halo.domain.model.anniversary.UpcomingAnniversary
import com.umc.halo.domain.repository.anniversary.AnniversaryRepository
import com.umc.halo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class AnniversaryViewModel @Inject constructor(
    private val anniversaryRepository: AnniversaryRepository
) : BaseViewModel<AnniversaryUiState, AnniversaryUiEvent>(
    initialState = AnniversaryUiState()
) {
    private var clearLastAddedHighlightJob: Job? = null

    override fun onEvent(event: AnniversaryUiEvent) {
        when (event) {
            AnniversaryUiEvent.ScreenEntered -> loadAnniversaries(showError = true)
            AnniversaryUiEvent.BackClicked -> handleBack()
            AnniversaryUiEvent.AddClicked -> openAdd()
            AnniversaryUiEvent.ListExited -> {
                clearLastAddedHighlightJob?.cancel()
                updateState {
                    copy(lastAddedId = null)
                }
            }

            AnniversaryUiEvent.SelectModeClicked -> updateState {
                copy(
                    isSelectionModeActive = !isSelectionModeActive,
                    selectedIds = emptySet()
                )
            }

            AnniversaryUiEvent.DeleteSelectedClicked -> deleteSelected()
            AnniversaryUiEvent.EditClicked -> currentState.openedItem
                ?.takeIf { !it.isOfficial }
                ?.id
                ?.let { openEdit(it) }

            AnniversaryUiEvent.ErrorMessageShown -> updateState {
                copy(errorMessage = null)
            }

            is AnniversaryUiEvent.AnniversaryClicked -> {
                if (currentState.isSelectionModeActive) {
                    toggleSelection(event.id)
                } else {
                    openDetail(event.id)
                }
            }

            is AnniversaryUiEvent.UpcomingClicked -> openDetail(event.id)
            is AnniversaryUiEvent.SelectionToggled -> toggleSelection(event.id)

            is AnniversaryUiEvent.TitleChanged -> updateState {
                copy(form = form.copy(title = event.title.take(TITLE_MAX_LENGTH)))
            }

            AnniversaryUiEvent.DateFieldClicked -> updateState {
                copy(form = form.copy(isCalendarExpanded = !form.isCalendarExpanded))
            }

            AnniversaryUiEvent.PreviousMonthClicked -> moveVisibleMonth(-1)
            AnniversaryUiEvent.NextMonthClicked -> moveVisibleMonth(1)

            is AnniversaryUiEvent.CalendarTypeChanged -> updateState {
                copy(
                    form = form.copy(
                        calendarType = event.type
                    )
                )
            }

            is AnniversaryUiEvent.RepeatChanged -> updateState {
                copy(
                    form = form.copy(
                        repeatEnabled = event.enabled
                    )
                )
            }

            is AnniversaryUiEvent.DateSelected -> updateState {
                copy(
                    form = form.copy(
                        date = event.date,
                        visibleYear = event.date.year,
                        visibleMonth = event.date.month,
                        isCalendarExpanded = true
                    )
                )
            }

            is AnniversaryUiEvent.D7AlarmChanged -> updateState {
                copy(form = form.copy(d7AlarmEnabled = event.enabled))
            }

            is AnniversaryUiEvent.DayAlarmChanged -> updateState {
                copy(form = form.copy(dayAlarmEnabled = event.enabled))
            }

            is AnniversaryUiEvent.MemoChanged -> updateState {
                copy(form = form.copy(memo = event.memo.take(MEMO_MAX_LENGTH)))
            }

            AnniversaryUiEvent.SaveClicked -> saveForm()
        }
    }

    private fun loadAnniversaries(showError: Boolean) {
        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }

            runCatching { anniversaryRepository.getAnniversaries() }
                .onSuccess { overview ->
                    updateState {
                        copy(
                            upcomingItems = overview.toUpcomingItems(),
                            personalItems = overview.toPersonalItems(),
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
                .onFailure {
                    updateState {
                        copy(
                            isLoading = false,
                            errorMessage = if (showError) LOAD_FAILED_MESSAGE else null
                        )
                    }
                }
        }
    }

    private fun handleBack() {
        updateState {
            when (mode) {
                AnniversaryScreenMode.LIST -> this
                else -> copy(
                    mode = AnniversaryScreenMode.LIST,
                    openedItem = null,
                    isSelectionModeActive = false,
                    selectedIds = emptySet(),
                    form = AnniversaryFormState()
                )
            }
        }
    }

    private fun openAdd() {
        updateState {
            copy(
                mode = AnniversaryScreenMode.ADD,
                openedItem = null,
                isSelectionModeActive = false,
                selectedIds = emptySet(),
                lastAddedId = null,
                form = AnniversaryFormState()
            )
        }
    }

    private fun openDetail(id: Long) {
        val item = findAnniversary(id) ?: return
        updateState {
            copy(
                mode = AnniversaryScreenMode.DETAIL,
                openedItem = item,
                isSelectionModeActive = false,
                selectedIds = emptySet(),
                lastAddedId = null
            )
        }
    }

    private fun openEdit(id: Long) {
        val item = findAnniversary(id)?.takeIf { !it.isOfficial } ?: return
        updateState {
            copy(
                mode = AnniversaryScreenMode.EDIT,
                openedItem = item,
                isSelectionModeActive = false,
                selectedIds = emptySet(),
                lastAddedId = null,
                form = AnniversaryFormState(
                    editingId = item.id,
                    title = item.title,
                    date = item.date,
                    calendarType = item.calendarType,
                    repeatEnabled = item.repeatEnabled,
                    d7AlarmEnabled = item.d7AlarmEnabled,
                    dayAlarmEnabled = item.dayAlarmEnabled,
                    memo = item.memo,
                    visibleYear = item.date.year,
                    visibleMonth = item.date.month
                )
            )
        }
    }

    private fun toggleSelection(id: Long) {
        if (id <= 0L) return
        updateState {
            val next = if (id in selectedIds) {
                selectedIds - id
            } else {
                selectedIds + id
            }
            copy(selectedIds = next)
        }
    }

    private fun findAnniversary(id: Long): AnniversaryItem? =
        (currentState.visibleUpcomingItems + currentState.personalItems)
            .firstOrNull { it.id == id }

    private fun deleteSelected() {
        val idsToDelete = currentState.selectedIds.filter { it > 0L }
        if (idsToDelete.isEmpty()) {
            updateState {
                copy(
                    isSelectionModeActive = false,
                    selectedIds = emptySet()
                )
            }
            return
        }

        viewModelScope.launch {
            updateState { copy(isSaving = true, errorMessage = null) }
            runCatching { anniversaryRepository.deleteAnniversaries(idsToDelete) }
                .onSuccess {
                    updateState {
                        copy(
                            personalItems = personalItems.filterNot { it.id in idsToDelete },
                            upcomingItems = upcomingItems.filterNot { it.id in idsToDelete },
                            isSelectionModeActive = false,
                            selectedIds = emptySet(),
                            lastAddedId = null,
                            isSaving = false
                        )
                    }
                    loadAnniversaries(showError = false)
                }
                .onFailure {
                    updateState {
                        copy(
                            isSaving = false,
                            errorMessage = DELETE_FAILED_MESSAGE
                        )
                    }
                }
        }
    }

    private fun moveVisibleMonth(delta: Int) {
        updateState {
            val total = form.visibleYear * 12 + (form.visibleMonth - 1) + delta
            copy(
                form = form.copy(
                    visibleYear = total / 12,
                    visibleMonth = total % 12 + 1
                )
            )
        }
    }

    private fun saveForm() {
        val form = currentState.form
        val date = form.date ?: return
        if (!form.canSave || currentState.isSaving) return

        val saveForm = AnniversarySaveForm(
            title = form.title.trim(),
            anniversaryDate = date.toApiDate(),
            isLunar = form.calendarType == AnniversaryCalendarType.LUNAR,
            isRepeated = form.repeatEnabled,
            sevenDaysAlarmEnabled = form.d7AlarmEnabled,
            dayAlarmEnabled = form.dayAlarmEnabled,
            memo = form.memo.trim().takeIf { it.isNotEmpty() }
        )

        viewModelScope.launch {
            updateState { copy(isSaving = true, errorMessage = null) }

            runCatching {
                form.editingId?.let { id ->
                    anniversaryRepository.updateAnniversary(id, saveForm)
                } ?: anniversaryRepository.createAnniversary(saveForm)
            }
                .onSuccess { savedId ->
                    val shouldHighlightAddedItem = form.editingId == null
                    updateState {
                        copy(
                            mode = AnniversaryScreenMode.LIST,
                            openedItem = null,
                            form = AnniversaryFormState(),
                            isSelectionModeActive = false,
                            selectedIds = emptySet(),
                            lastAddedId = if (shouldHighlightAddedItem) savedId else null,
                            isSaving = false
                        )
                    }
                    if (shouldHighlightAddedItem) {
                        scheduleLastAddedHighlightClear(savedId)
                    }
                    loadAnniversaries(showError = false)
                }
                .onFailure {
                    updateState {
                        copy(
                            isSaving = false,
                            errorMessage = it.message?.takeIf { message -> message.isNotBlank() }
                                ?: SAVE_FAILED_MESSAGE
                        )
                    }
                }
        }
    }

    private fun scheduleLastAddedHighlightClear(addedId: Long) {
        clearLastAddedHighlightJob?.cancel()
        clearLastAddedHighlightJob = viewModelScope.launch {
            delay(ADDED_HIGHLIGHT_DURATION_MS)
            updateState {
                if (lastAddedId == addedId) {
                    copy(lastAddedId = null)
                } else {
                    this
                }
            }
        }
    }

    private companion object {
        const val TITLE_MAX_LENGTH = 20
        const val MEMO_MAX_LENGTH = 50
        const val ADDED_HIGHLIGHT_DURATION_MS = 2_000L
        const val LOAD_FAILED_MESSAGE = "기념일 정보를 불러오지 못했어요."
        const val SAVE_FAILED_MESSAGE = "기념일을 저장하지 못했어요."
        const val DELETE_FAILED_MESSAGE = "기념일을 삭제하지 못했어요."
    }
}

private fun AnniversaryOverview.toPersonalItems(): List<AnniversaryItem> =
    myAnniversaries.sortedByDescending { it.anniversaryId }.map { anniversary ->
        val originalDate = anniversary.anniversaryDate.toAnniversaryDate()
        AnniversaryItem(
            id = anniversary.anniversaryId,
            title = anniversary.title,
            date = originalDate,
            displayDate = anniversary.displayDate?.toAnniversaryDate() ?: originalDate,
            calendarType = if (anniversary.isLunar) {
                AnniversaryCalendarType.LUNAR
            } else {
                AnniversaryCalendarType.SOLAR
            },
            memo = anniversary.memo.orEmpty(),
            repeatEnabled = anniversary.isRepeated,
            d7AlarmEnabled = anniversary.sevenDaysAlarmEnabled,
            dayAlarmEnabled = anniversary.dayAlarmEnabled,
            isOfficial = false
        )
    }

private fun AnniversaryOverview.toUpcomingItems(): List<AnniversaryItem> {
    val personalItemsById = toPersonalItems().associateBy { it.id }
    val commonItems = commonAnniversaries.associateBy { it.commonAnniversaryId }

    return upcomingAnniversaries
        .filter { it.dDay in 0..7 }
        .sortedBy { it.dDay }
        .map { anniversary ->
            val upcomingDate = anniversary.anniversaryDate.toAnniversaryDate()
            val personalItem = anniversary.anniversaryId?.let { personalItemsById[it] }
            val commonItem = anniversary.commonAnniversaryId?.let { commonItems[it] }
                ?: commonAnniversaries.firstOrNull { it.title == anniversary.title }

            when {
                personalItem != null -> personalItem.copy(
                    displayDate = upcomingDate,
                    dDayLabel = anniversary.dDay.toDdayLabel()
                )

                commonItem != null -> commonItem.toAnniversaryItem(
                    upcoming = anniversary,
                    upcomingDate = upcomingDate
                )

                else -> AnniversaryItem(
                    id = -abs(anniversary.title.hashCode().toLong()),
                    title = anniversary.title,
                    date = upcomingDate,
                    displayDate = upcomingDate,
                    dDayLabel = anniversary.dDay.toDdayLabel(),
                    isOfficial = true
                )
            }
        }
}

private fun CommonAnniversary.toAnniversaryItem(
    upcoming: UpcomingAnniversary,
    upcomingDate: AnniversaryDate
): AnniversaryItem = AnniversaryItem(
    id = -commonAnniversaryId,
    title = title,
    date = upcomingDate,
    displayDate = upcomingDate,
    calendarType = if (isLunar) AnniversaryCalendarType.LUNAR else AnniversaryCalendarType.SOLAR,
    dDayLabel = upcoming.dDay.toDdayLabel(),
    memo = memo.orEmpty(),
    repeatEnabled = true,
    d7AlarmEnabled = sevenDaysAlarmEnabled,
    dayAlarmEnabled = dayAlarmEnabled,
    isOfficial = true
)

private fun String.toAnniversaryDate(): AnniversaryDate {
    val parts = split("-")
    return AnniversaryDate(
        year = parts.getOrNull(0)?.toIntOrNull() ?: 1970,
        month = parts.getOrNull(1)?.toIntOrNull() ?: 1,
        day = parts.getOrNull(2)?.toIntOrNull() ?: 1
    )
}

private fun AnniversaryDate.toApiDate(): String =
    "${year.toString().padStart(4, '0')}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"
