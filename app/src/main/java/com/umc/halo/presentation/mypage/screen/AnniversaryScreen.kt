package com.umc.halo.presentation.mypage.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.halo.R
import com.umc.halo.presentation.component.haloCardShadow
import com.umc.halo.presentation.mypage.anniversary.AnniversaryCalendarType
import com.umc.halo.presentation.mypage.anniversary.AnniversaryDate
import com.umc.halo.presentation.mypage.anniversary.AnniversaryFormState
import com.umc.halo.presentation.mypage.anniversary.AnniversaryItem
import com.umc.halo.presentation.mypage.anniversary.AnniversaryScreenMode
import com.umc.halo.presentation.mypage.anniversary.AnniversaryUiEvent
import com.umc.halo.presentation.mypage.anniversary.AnniversaryUiState
import com.umc.halo.presentation.mypage.component.HaloSwitch
import com.umc.halo.presentation.mypage.component.MyPageContainer
import com.umc.halo.presentation.mypage.component.MyPageTopBar
import com.umc.halo.presentation.mypage.component.PrimaryActionButton
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray200
import com.umc.halo.presentation.theme.Gray300
import com.umc.halo.presentation.theme.Gray30
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.Gray600
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary30
import com.umc.halo.presentation.theme.Primary100
import com.umc.halo.presentation.theme.Primary50
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.Primary600
import com.umc.halo.presentation.theme.White
import java.util.Calendar

private val AddButtonShadowColor = Color(0x409A9A9A)

@Composable
fun AnniversaryScreen(
    uiState: AnniversaryUiState,
    onEvent: (AnniversaryUiEvent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    MyPageContainer(modifier = modifier) {
        when (uiState.mode) {
            AnniversaryScreenMode.LIST -> AnniversaryListScreen(
                uiState = uiState,
                onEvent = onEvent,
                onBack = onBack
            )

            AnniversaryScreenMode.ADD -> AnniversaryFormScreen(
                title = "기념일 추가",
                form = uiState.form,
                today = uiState.today,
                isSaving = uiState.isSaving,
                onEvent = onEvent,
                onBack = { onEvent(AnniversaryUiEvent.BackClicked) }
            )

            AnniversaryScreenMode.DETAIL -> AnniversaryDetailScreen(
                item = uiState.openedItem,
                onBack = { onEvent(AnniversaryUiEvent.BackClicked) },
                onEdit = { onEvent(AnniversaryUiEvent.EditClicked) }
            )

            AnniversaryScreenMode.EDIT -> AnniversaryFormScreen(
                title = "",
                form = uiState.form,
                today = uiState.today,
                isSaving = uiState.isSaving,
                onEvent = onEvent,
                onBack = { onEvent(AnniversaryUiEvent.BackClicked) }
            )
        }
    }
}

@Composable
private fun AnniversaryListScreen(
    uiState: AnniversaryUiState,
    onEvent: (AnniversaryUiEvent) -> Unit,
    onBack: () -> Unit
) {
    val actualSelectedIds = uiState.selectedIds

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 148.dp)
        ) {
            item {
                MyPageTopBar(title = "기념일 관리", onBack = onBack)
                Spacer(Modifier.height(32.dp))
                AnniversarySectionTitle(
                    text = "다가오는 기념일",
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(Modifier.height(20.dp))
            }

            item {
                if (uiState.visibleUpcomingItems.isEmpty()) {
                    AnniversaryEmptyMessage(
                        text = "다가오는 기념일이 없습니다.",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(108.dp)
                            .padding(horizontal = 24.dp)
                    )
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 2.dp),
                        horizontalArrangement = Arrangement.spacedBy(18.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(112.dp)
                    ) {
                        items(uiState.visibleUpcomingItems) { item ->
                            UpcomingAnniversaryCard(
                                item = item,
                                onClick = { onEvent(AnniversaryUiEvent.UpcomingClicked(item.id)) }
                            )
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(44.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnniversarySectionTitle("기념일 관리")
                    Spacer(Modifier.weight(1f))
                    AnniversaryPillButton(
                        text = when {
                            !uiState.isSelectionMode -> "선택"
                            actualSelectedIds.isEmpty() -> "취소"
                            else -> "삭제"
                        },
                        active = actualSelectedIds.isNotEmpty(),
                        onClick = {
                            if (uiState.isSelectionMode) {
                                onEvent(AnniversaryUiEvent.DeleteSelectedClicked)
                            } else {
                                onEvent(AnniversaryUiEvent.SelectModeClicked)
                            }
                        }
                    )
                }
                Spacer(Modifier.height(18.dp))
            }

            if (uiState.personalItems.isEmpty()) {
                item {
                    AnniversaryEmptyMessage(
                        text = "기념일을 등록해주세요!",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(132.dp)
                            .padding(horizontal = 24.dp)
                    )
                }
            } else {
                items(uiState.personalItems, key = { it.id }) { item ->
                    AnniversaryListItem(
                        item = item,
                        isSelectionMode = uiState.isSelectionMode,
                        selected = item.id in actualSelectedIds,
                        highlighted = uiState.lastAddedId == item.id,
                        onClick = { onEvent(AnniversaryUiEvent.AnniversaryClicked(item.id)) },
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
        }

        if (!uiState.isSelectionMode) {
            val addButtonShape = RoundedCornerShape(100.dp)

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 28.dp, bottom = 36.dp)
                    .width(121.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = addButtonShape,
                        clip = false,
                        ambientColor = AddButtonShadowColor,
                        spotColor = AddButtonShadowColor
                    )
                    .clickable { onEvent(AnniversaryUiEvent.AddClicked) },
                shape = addButtonShape,
                color = Primary500
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "일정 추가",
                        style = HaloType.body01Medium,
                        color = White,
                        maxLines = 1
                    )
                    Box(
                        modifier = Modifier.size(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_anniversary_add),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UpcomingAnniversaryCard(
    item: AnniversaryItem,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)

    Surface(
        modifier = Modifier
            .width(168.dp)
            .height(108.dp)
            .haloCardShadow(shape)
            .clickable(onClick = onClick),
        shape = shape,
        color = White
    ) {
        Row(
            modifier = Modifier.padding(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (item.dDayLabel == "D-DAY") Primary50 else Gray30
                ) {
                    Text(
                        text = item.dDayLabel ?: "D-7",
                        style = HaloType.body02SemiBold.copy(fontSize = 15.sp),
                        color = if (item.dDayLabel == "D-DAY") Primary600 else Gray600,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp)
                    )
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    text = item.title,
                    style = HaloType.heading03SemiBold,
                    color = Gray800,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = item.displayDate.compactWithDayOfWeek(),
                    style = HaloType.body03Regular.copy(fontSize = 13.sp),
                    color = Gray600
                )
            }
            RightChevron(tint = Gray800)
        }
    }
}

@Composable
private fun AnniversaryListItem(
    item: AnniversaryItem,
    isSelectionMode: Boolean,
    selected: Boolean,
    highlighted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(66.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = when {
            selected -> Primary30
            highlighted -> Primary50
            else -> Gray30
        }
    ) {
        Row(
            modifier = Modifier.padding(start = 24.dp, end = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = HaloType.body01SemiBold,
                    color = if (selected) Primary600 else Gray800
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = item.displayDate.compactWithDayOfWeek(),
                    style = HaloType.caption01Medium.copy(fontSize = 11.5.sp),
                    color = if (selected) Primary600 else Gray600
                )
            }
            if (isSelectionMode) {
                Text(
                    text = "✓",
                    style = HaloType.body01SemiBold.copy(fontSize = 23.sp),
                    color = if (selected) Primary600 else Gray300
                )
            } else {
                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    RightChevron(tint = Gray500)
                }
            }
        }
    }
}

@Composable
private fun AnniversaryEmptyMessage(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = HaloType.body03Medium,
            color = Gray500,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AnniversaryFormScreen(
    title: String,
    form: AnniversaryFormState,
    today: AnniversaryDate,
    isSaving: Boolean,
    onEvent: (AnniversaryUiEvent) -> Unit,
    onBack: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 112.dp)
        ) {
            item {
                MyPageTopBar(title = title, onBack = onBack)
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Spacer(Modifier.height(28.dp))
                    AnniversaryInputLabel("기념일명")
                    Spacer(Modifier.height(12.dp))
                    AnniversaryTextField(
                        value = form.title,
                        placeholder = "기록해보세요.",
                        onValueChange = { onEvent(AnniversaryUiEvent.TitleChanged(it)) },
                        maxLength = 20,
                        showCounter = false
                    )
                    Spacer(Modifier.height(12.dp))
                    AnniversaryRepeatInputRow(
                        checked = form.repeatEnabled,
                        onCheckedChange = { onEvent(AnniversaryUiEvent.RepeatChanged(it)) }
                    )
                    Spacer(Modifier.height(28.dp))
                    AnniversaryInputLabel("날짜")
                    Spacer(Modifier.height(12.dp))
                    AnniversaryDateField(
                        form = form,
                        onClick = { onEvent(AnniversaryUiEvent.DateFieldClicked) }
                    )
                    if (form.isCalendarExpanded) {
                        Spacer(Modifier.height(18.dp))
                        AnniversaryCalendar(
                            form = form,
                            today = today,
                            onEvent = onEvent
                        )
                    }
                    Spacer(Modifier.height(28.dp))
                    AnniversaryInputLabel("알림설정")
                    Spacer(Modifier.height(12.dp))
                    AnniversaryAlarmBox(
                        d7Checked = form.d7AlarmEnabled,
                        dayChecked = form.dayAlarmEnabled,
                        onD7Changed = { onEvent(AnniversaryUiEvent.D7AlarmChanged(it)) },
                        onDayChanged = { onEvent(AnniversaryUiEvent.DayAlarmChanged(it)) }
                    )
                    Spacer(Modifier.height(28.dp))
                    AnniversaryInputLabel("메모")
                    Spacer(Modifier.height(12.dp))
                    AnniversaryTextField(
                        value = form.memo,
                        placeholder = "메모를 입력해주세요.",
                        onValueChange = { onEvent(AnniversaryUiEvent.MemoChanged(it)) },
                        minHeight = 104.dp,
                        singleLine = false,
                        maxLength = 50,
                        showCounter = true
                    )
                }
            }
        }

        PrimaryActionButton(
            text = "저장",
            onClick = { onEvent(AnniversaryUiEvent.SaveClicked) },
            enabled = form.canSave && !isSaving,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp, vertical = 24.dp)
                .navigationBarsPadding()
                .widthIn(max = 372.dp)
        )
    }
}

@Composable
private fun AnniversaryDetailScreen(
    item: AnniversaryItem?,
    onBack: () -> Unit,
    onEdit: () -> Unit
) {
    val anniversary = item ?: return

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 99.dp)
    ) {
        item {
            AnniversaryDetailTopBar(
                onBack = onBack,
                onEdit = onEdit,
                showEdit = !anniversary.isOfficial
            )
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Spacer(Modifier.height(28.dp))
                AnniversaryInputLabel("기념일명")
                Spacer(Modifier.height(12.dp))
                ReadOnlyBox(text = anniversary.title)
                Spacer(Modifier.height(12.dp))
                AnniversaryRepeatReadOnlyRow(checked = anniversary.repeatEnabled)
                Spacer(Modifier.height(28.dp))
                AnniversaryInputLabel("날짜")
                Spacer(Modifier.height(12.dp))
                ReadOnlyBox(
                    text = anniversary.date.formatted(),
                    trailing = anniversary.calendarType.label
                )
                Spacer(Modifier.height(28.dp))
                AnniversaryInputLabel("알림설정")
                Spacer(Modifier.height(12.dp))
                AnniversaryReadOnlyAlarmBox(
                    d7Checked = anniversary.d7AlarmEnabled,
                    dayChecked = anniversary.dayAlarmEnabled
                )
                Spacer(Modifier.height(28.dp))
                AnniversaryInputLabel("메모")
                Spacer(Modifier.height(12.dp))
                AnniversaryMemoReadOnlyBox(text = anniversary.memo)
            }
        }
    }
}

@Composable
private fun AnniversaryDetailTopBar(
    onBack: () -> Unit,
    onEdit: () -> Unit,
    showEdit: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 8.dp)
                .size(44.dp)
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_common_chevron_left),
                contentDescription = null,
                tint = Gray800,
                modifier = Modifier.size(8.dp, 12.dp)
            )
        }
        if (showEdit) {
            Surface(
                shape = RoundedCornerShape(100.dp),
                color = Gray30,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 24.dp)
                    .clickable(onClick = onEdit)
            ) {
                Text(
                    text = "편집",
                    style = HaloType.body02Regular,
                    color = Gray500,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun AnniversaryCalendar(
    form: AnniversaryFormState,
    today: AnniversaryDate,
    onEvent: (AnniversaryUiEvent) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_common_chevron_left),
                contentDescription = null,
                tint = Gray500,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onEvent(AnniversaryUiEvent.PreviousMonthClicked) }
                    .padding(6.dp)
            )
            Text(
                text = "${form.visibleYear}년 ${form.visibleMonth}월",
                style = HaloType.body01Medium,
                color = Gray500,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_common_chevron_right),
                contentDescription = null,
                tint = Gray500,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onEvent(AnniversaryUiEvent.NextMonthClicked) }
                    .padding(6.dp)
            )
            Spacer(Modifier.weight(1f))
            CalendarTypeSegmentedControl(
                selectedType = form.calendarType,
                onTypeSelected = { onEvent(AnniversaryUiEvent.CalendarTypeChanged(it)) }
            )
        }
        Spacer(Modifier.height(16.dp))
        CalendarGrid(
            form = form,
            today = today,
            onDateSelected = { onEvent(AnniversaryUiEvent.DateSelected(it)) }
        )
    }
}

@Composable
private fun CalendarGrid(
    form: AnniversaryFormState,
    today: AnniversaryDate,
    onDateSelected: (AnniversaryDate) -> Unit
) {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, form.visibleYear)
        set(Calendar.MONTH, form.visibleMonth - 1)
        set(Calendar.DAY_OF_MONTH, 1)
    }
    val firstDayOffset = calendar.get(Calendar.DAY_OF_WEEK) - 1
    val lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val cells = List(firstDayOffset) { 0 } + (1..lastDay).toList()
    val rows = cells.chunked(7)

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT").forEach {
                Text(
                    text = it,
                    style = HaloType.caption01Medium.copy(fontSize = 11.sp),
                    color = Gray500,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        rows.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth()) {
                (row + List(7 - row.size) { 0 }).forEach { day ->
                    val date = AnniversaryDate(
                        year = form.visibleYear,
                        month = form.visibleMonth,
                        day = day
                    )
                    val selected = form.date?.let {
                        it.year == form.visibleYear && it.month == form.visibleMonth && it.day == day
                    } == true
                    val isSolarCalendar = form.calendarType == AnniversaryCalendarType.SOLAR
                    val isToday = day > 0 && isSolarCalendar && date == today
                    val enabled = day > 0
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(34.dp)
                            .clickable(enabled = enabled) {
                                onDateSelected(date)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape),
                            shape = CircleShape,
                            color = if (selected) Primary500 else Color.Transparent,
                            border = if (isToday && !selected) {
                                BorderStroke(1.dp, Primary500)
                            } else {
                                null
                            }
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (day == 0) "" else day.toString(),
                                    style = if (selected) HaloType.body02Medium else HaloType.body02Regular,
                                    color = when {
                                        selected -> White
                                        isToday -> Primary500
                                        else -> Gray500
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnniversaryAlarmBox(
    d7Checked: Boolean,
    dayChecked: Boolean,
    onD7Changed: (Boolean) -> Unit,
    onDayChanged: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Gray30
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)) {
            AlarmRow(
                text = "D-7 알림",
                checked = d7Checked,
                onCheckedChange = onD7Changed,
                enabled = enabled
            )
            Spacer(Modifier.height(10.dp))
            AlarmRow(
                text = "당일 알림",
                checked = dayChecked,
                onCheckedChange = onDayChanged,
                enabled = enabled
            )
        }
    }
}

@Composable
private fun AlarmRow(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = HaloType.body02Medium,
            color = if (enabled) Gray500 else Gray400,
            modifier = Modifier.weight(1f)
        )
        HaloSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
    }
}

@Composable
private fun AnniversaryRepeatInputRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "기념일 반복 여부",
            style = HaloType.body02Medium,
            color = Gray500
        )
        Spacer(Modifier.width(8.dp))
        HaloSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun AnniversaryRepeatReadOnlyRow(
    checked: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "기념일 반복 여부",
            style = HaloType.body02Medium,
            color = Gray500
        )
        Spacer(Modifier.width(8.dp))
        ReadOnlySwitch(checked = checked)
    }
}

@Composable
private fun AnniversaryReadOnlyAlarmBox(
    d7Checked: Boolean,
    dayChecked: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Gray30
    ) {
        Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 18.dp)) {
            ReadOnlyAlarmRow(
                text = "D-7 알림",
                checked = d7Checked
            )
            Spacer(Modifier.height(18.dp))
            ReadOnlyAlarmRow(
                text = "당일 알림",
                checked = dayChecked
            )
        }
    }
}

@Composable
private fun ReadOnlyAlarmRow(
    text: String,
    checked: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = HaloType.body02Medium,
            color = Gray500,
            modifier = Modifier.weight(1f)
        )
        ReadOnlySwitch(checked = checked)
    }
}

@Composable
private fun ReadOnlySwitch(
    checked: Boolean
) {
    Box(
        modifier = Modifier
            .width(42.dp)
            .height(24.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(if (checked) Gray600 else Gray100)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = if (checked) 21.dp else 3.dp)
                .size(18.dp)
                .clip(CircleShape)
                .background(if (checked) White else Gray300)
        )
    }
}

@Composable
private fun AnniversaryDateField(
    form: AnniversaryFormState,
    onClick: () -> Unit
) {
    val hasDate = form.date != null
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = Gray30
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = form.date?.formatted() ?: "날짜를 선택해주세요.",
                style = if (hasDate) HaloType.body02Medium else HaloType.body02Regular,
                color = if (hasDate) Gray800 else Gray300,
                modifier = Modifier.weight(1f)
            )
            if (hasDate) {
                Text(
                    text = form.calendarType.label,
                    style = HaloType.body02Regular,
                    color = Gray300
                )
                Spacer(Modifier.width(12.dp))
            }
            Box(
                modifier = Modifier.size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_common_chevron_right),
                    contentDescription = null,
                    tint = if (hasDate) Gray500 else Gray300,
                    modifier = Modifier
                        .size(8.dp, 12.dp)
                        .graphicsLayer(rotationZ = if (form.isCalendarExpanded) -90f else 90f)
                )
            }
        }
    }
}

@Composable
private fun AnniversaryTextField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    minHeight: androidx.compose.ui.unit.Dp = 60.dp,
    singleLine: Boolean = true,
    maxLength: Int? = null,
    showCounter: Boolean = false
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = minHeight),
        shape = RoundedCornerShape(12.dp),
        color = Gray30
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = minHeight)
                .padding(horizontal = 24.dp, vertical = if (singleLine) 0.dp else 18.dp),
            contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart
        ) {
            BasicTextField(
                value = value,
                onValueChange = { input ->
                    onValueChange(maxLength?.let { input.take(it) } ?: input)
                },
                singleLine = singleLine,
                textStyle = HaloType.body02Medium.copy(color = Gray800).toTextStyle(),
                modifier = Modifier.fillMaxWidth()
            )
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = HaloType.body02Regular,
                    color = Gray300
                )
            }
            if (showCounter && maxLength != null) {
                Text(
                    text = "${value.length}/$maxLength",
                    style = HaloType.body02Regular,
                    color = Gray300,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }
    }
}

@Composable
private fun ReadOnlyBox(
    text: String,
    trailing: String? = null,
    minHeight: androidx.compose.ui.unit.Dp = 60.dp,
    alignTop: Boolean = false
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = minHeight),
        shape = RoundedCornerShape(12.dp),
        color = Gray30
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = if (alignTop) 18.dp else 0.dp),
            verticalAlignment = if (alignTop) Alignment.Top else Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = HaloType.body02Regular,
                color = Gray800,
                modifier = Modifier.weight(1f)
            )
            if (trailing != null) {
                Text(
                    text = trailing,
                    style = HaloType.caption01Medium.copy(fontSize = 11.5.sp),
                    color = Gray300
                )
            }
        }
    }
}

@Composable
private fun AnniversaryMemoReadOnlyBox(
    text: String,
    maxLength: Int = 50
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp),
        shape = RoundedCornerShape(16.dp),
        color = Gray30
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 18.dp)
        ) {
            Text(
                text = text,
                style = HaloType.body02Medium,
                color = Gray800,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.align(Alignment.TopStart)
            )
            Text(
                text = "${text.length}/$maxLength",
                style = HaloType.body02Regular,
                color = Gray400,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}

@Composable
private fun AnniversarySectionTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = HaloType.heading03SemiBold,
        color = Gray800,
        modifier = modifier
    )
}

@Composable
private fun AnniversaryInputLabel(text: String) {
    Text(
        text = text,
        style = HaloType.body01SemiBold,
        color = Gray800
    )
}

@Composable
private fun AnniversaryPillButton(
    text: String,
    active: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(100.dp),
        color = if (active) Primary100 else Gray30,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            style = if (active) HaloType.body02Medium else HaloType.body02Regular,
            color = if (active) Primary500 else Gray500,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun CalendarTypeSegmentedControl(
    selectedType: AnniversaryCalendarType,
    onTypeSelected: (AnniversaryCalendarType) -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .background(Gray30)
    ) {
        CalendarTypeSegment(
            text = "양력",
            selected = selectedType == AnniversaryCalendarType.SOLAR,
            onClick = { onTypeSelected(AnniversaryCalendarType.SOLAR) }
        )
        CalendarTypeSegment(
            text = "음력",
            selected = selectedType == AnniversaryCalendarType.LUNAR,
            onClick = { onTypeSelected(AnniversaryCalendarType.LUNAR) }
        )
    }
}

@Composable
private fun CalendarTypeSegment(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .background(if (selected) Primary50 else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(
                start = if (selected) 15.dp else 14.dp,
                end = if (selected) 11.dp else 15.dp,
                top = 7.dp,
                bottom = 7.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = HaloType.body02Medium,
            color = if (selected) Primary500 else Gray200
        )
    }
}

@Composable
private fun CalendarTypeChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (selected) Primary50 else Gray30,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            style = HaloType.caption01Medium.copy(fontSize = 11.5.sp),
            color = if (selected) Primary600 else Gray300,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun RightChevron(tint: Color) {
    Icon(
        painter = painterResource(id = R.drawable.ic_common_chevron_right),
        contentDescription = null,
        tint = tint,
        modifier = Modifier.size(8.dp, 12.dp)
    )
}

private fun TextStyle.toTextStyle(): TextStyle = this
