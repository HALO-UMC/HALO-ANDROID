package com.umc.halo.presentation.mypage.anniversary

import com.umc.halo.presentation.base.BaseViewModel

class AnniversaryViewModel : BaseViewModel<AnniversaryUiState, AnniversaryUiEvent>(
    initialState = AnniversaryUiState()
) {
    private var nextId = 10L

    override fun onEvent(event: AnniversaryUiEvent) {
        when (event) {
            AnniversaryUiEvent.BackClicked -> handleBack()
            AnniversaryUiEvent.AddClicked -> openAdd()
            AnniversaryUiEvent.ListExited -> updateState {
                copy(lastAddedId = null)
            }
            AnniversaryUiEvent.SelectModeClicked -> updateState {
                copy(
                    isSelectionModeActive = !isSelectionModeActive,
                    selectedIds = emptySet()
                )
            }

            AnniversaryUiEvent.DeleteSelectedClicked -> deleteSelected()
            AnniversaryUiEvent.EditClicked -> currentState.openedItem?.id?.let { openEdit(it) }

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
                copy(form = form.copy(title = event.title))
            }

            AnniversaryUiEvent.DateFieldClicked -> updateState {
                copy(form = form.copy(isCalendarExpanded = !form.isCalendarExpanded))
            }

            AnniversaryUiEvent.PreviousMonthClicked -> moveVisibleMonth(-1)
            AnniversaryUiEvent.NextMonthClicked -> moveVisibleMonth(1)

            is AnniversaryUiEvent.CalendarTypeChanged -> updateState {
                copy(form = form.copy(calendarType = event.type))
            }

            is AnniversaryUiEvent.RepeatChanged -> updateState {
                copy(form = form.copy(repeatEnabled = event.enabled))
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
                copy(form = form.copy(memo = event.memo.take(50)))
            }

            AnniversaryUiEvent.SaveClicked -> saveForm()
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
        val item = findAnniversary(id) ?: return
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
        updateState {
            val idsToDelete = selectedIds.filter { it > 0 }.toSet()
            if (idsToDelete.isEmpty()) {
                copy(
                    isSelectionModeActive = false,
                    selectedIds = emptySet()
                )
            } else {
                copy(
                    personalItems = personalItems.filterNot { it.id in idsToDelete },
                    isSelectionModeActive = false,
                    selectedIds = emptySet(),
                    lastAddedId = null
                )
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
        if (form.title.isBlank()) return
        val previousItem = form.editingId?.let { findAnniversary(it) }

        val item = AnniversaryItem(
            id = form.editingId ?: nextId++,
            title = form.title,
            date = date,
            calendarType = form.calendarType,
            repeatEnabled = form.repeatEnabled,
            d7AlarmEnabled = form.d7AlarmEnabled,
            dayAlarmEnabled = form.dayAlarmEnabled,
            memo = form.memo,
            isOfficial = previousItem?.isOfficial ?: false
        )

        updateState {
            val nextPersonalItems = if (form.editingId == null) {
                listOf(item) + personalItems
            } else {
                personalItems.map { if (it.id == item.id) item else it }
            }
            val nextUpcomingItems = if (form.editingId == null) {
                upcomingItems
            } else {
                upcomingItems.map { if (it.id == item.id) item else it }
            }

            copy(
                mode = AnniversaryScreenMode.LIST,
                upcomingItems = nextUpcomingItems,
                personalItems = nextPersonalItems,
                openedItem = null,
                form = AnniversaryFormState(),
                isSelectionModeActive = false,
                selectedIds = emptySet(),
                lastAddedId = if (form.editingId == null) item.id else null
            )
        }
    }
}
