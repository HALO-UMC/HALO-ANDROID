package com.umc.halo.presentation.mypage.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.halo.R
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
import com.umc.halo.presentation.theme.Gray50
import com.umc.halo.presentation.theme.Gray600
import com.umc.halo.presentation.theme.Gray700
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary30
import com.umc.halo.presentation.theme.Primary50
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.Primary600
import com.umc.halo.presentation.theme.White
import java.util.Calendar

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
                title = "일정 추가",
                form = uiState.form,
                onEvent = onEvent,
                onBack = { onEvent(AnniversaryUiEvent.BackClicked) }
            )

            AnniversaryScreenMode.DETAIL -> AnniversaryDetailScreen(
                item = uiState.openedItem,
                onBack = { onEvent(AnniversaryUiEvent.BackClicked) }
            )

            AnniversaryScreenMode.EDIT -> AnniversaryFormScreen(
                title = "",
                form = uiState.form,
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
    val actualSelectedIds = uiState.selectedIds.filter { it > 0 }.toSet()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 24.dp,
                end = 24.dp,
                bottom = 116.dp
            )
        ) {
            item {
                MyPageTopBar(title = "기념일 관리", onBack = onBack)
                Spacer(Modifier.height(32.dp))
                AnniversarySectionTitle("다가오는 기념일")
                Spacer(Modifier.height(20.dp))
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(uiState.upcomingItems) { item ->
                        UpcomingAnniversaryCard(
                            item = item,
                            onClick = { onEvent(AnniversaryUiEvent.UpcomingClicked(item.id)) }
                        )
                    }
                }
            }

            item {
                Spacer(Modifier.height(44.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnniversarySectionTitle("기념일 관리")
                    Spacer(Modifier.weight(1f))
                    AnniversaryPillButton(
                        text = if (uiState.selectedIds.isEmpty()) "선택" else "삭제",
                        active = actualSelectedIds.isNotEmpty(),
                        onClick = {
                            if (uiState.selectedIds.isEmpty()) {
                                onEvent(AnniversaryUiEvent.SelectModeClicked)
                            } else {
                                onEvent(AnniversaryUiEvent.DeleteSelectedClicked)
                            }
                        }
                    )
                }
                Spacer(Modifier.height(18.dp))
            }

            items(uiState.personalItems, key = { it.id }) { item ->
                AnniversaryListItem(
                    item = item,
                    isSelectionMode = uiState.selectedIds.isNotEmpty(),
                    selected = item.id in actualSelectedIds,
                    highlighted = uiState.lastAddedId == item.id,
                    onClick = { onEvent(AnniversaryUiEvent.AnniversaryClicked(item.id)) }
                )
                Spacer(Modifier.height(12.dp))
            }
        }

        if (uiState.selectedIds.isEmpty()) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 24.dp, bottom = 38.dp)
                    .navigationBarsPadding()
                    .width(121.dp)
                    .height(54.dp)
                    .clickable { onEvent(AnniversaryUiEvent.AddClicked) },
                shape = RoundedCornerShape(100.dp),
                color = Primary500,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(start = 18.dp, end = 18.dp, top = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "일정 추가",
                        style = HaloType.body02Medium.copy(fontSize = 16.sp),
                        color = White,
                        maxLines = 1
                    )
                    Box(
                        modifier = Modifier.size(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+",
                            style = HaloType.heading03SemiBold.copy(fontSize = 24.sp),
                            color = White,
                            textAlign = TextAlign.Center
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
    Surface(
        modifier = Modifier
            .width(168.dp)
            .height(108.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = White,
        border = BorderStroke(1.dp, Gray50)
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
                    style = HaloType.body01SemiBold.copy(fontSize = 18.sp),
                    color = Gray800,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = item.date.compactWithDayOfWeek("화"),
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
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = if (selected || highlighted) Primary30 else Gray30
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = HaloType.body02SemiBold.copy(fontSize = 16.sp),
                    color = if (selected || highlighted) Primary600 else Gray800
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = item.date.compactWithDayOfWeek(),
                    style = HaloType.caption01Medium.copy(fontSize = 11.5.sp),
                    color = if (selected || highlighted) Primary600 else Gray600
                )
            }
            if (isSelectionMode) {
                Text(
                    text = "✓",
                    style = HaloType.body01SemiBold.copy(fontSize = 23.sp),
                    color = if (selected) Primary600 else Gray300
                )
            } else {
                RightChevron(tint = Gray500)
            }
        }
    }
}

@Composable
private fun AnniversaryFormScreen(
    title: String,
    form: AnniversaryFormState,
    onEvent: (AnniversaryUiEvent) -> Unit,
    onBack: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 24.dp,
                end = 24.dp,
                bottom = 112.dp
            )
        ) {
            item {
                MyPageTopBar(title = title, onBack = onBack)
                Spacer(Modifier.height(28.dp))
                AnniversaryInputLabel("기념일명")
                Spacer(Modifier.height(12.dp))
                AnniversaryTextField(
                    value = form.title,
                    placeholder = "기록해보세요.",
                    onValueChange = { onEvent(AnniversaryUiEvent.TitleChanged(it)) }
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
                    singleLine = false
                )
            }
        }

        PrimaryActionButton(
            text = "저장",
            onClick = { onEvent(AnniversaryUiEvent.SaveClicked) },
            enabled = form.canSave,
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
    onBack: () -> Unit
) {
    val anniversary = item ?: return

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            start = 24.dp,
            end = 24.dp,
            bottom = 40.dp
        )
    ) {
        item {
            MyPageTopBar(title = "", onBack = onBack)
            Spacer(Modifier.height(28.dp))
            AnniversaryInputLabel("기념일명")
            Spacer(Modifier.height(12.dp))
            ReadOnlyBox(text = anniversary.title)
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
            AnniversaryAlarmBox(
                d7Checked = anniversary.d7AlarmEnabled,
                dayChecked = anniversary.dayAlarmEnabled,
                onD7Changed = {},
                onDayChanged = {},
                enabled = false
            )
            Spacer(Modifier.height(28.dp))
            AnniversaryInputLabel("메모")
            Spacer(Modifier.height(12.dp))
            ReadOnlyBox(
                text = anniversary.memo.ifBlank { "아버지 생신 챙겨드려야지!" },
                minHeight = 104.dp,
                alignTop = true
            )
        }
    }
}

@Composable
private fun AnniversaryCalendar(
    form: AnniversaryFormState,
    onEvent: (AnniversaryUiEvent) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_common_backward_arrow),
                contentDescription = null,
                tint = Gray500,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onEvent(AnniversaryUiEvent.PreviousMonthClicked) }
                    .padding(6.dp)
            )
            Text(
                text = "${form.visibleYear}년 ${form.visibleMonth}월",
                style = HaloType.body02Medium.copy(fontSize = 16.sp),
                color = Gray600,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_home_right_arrow),
                contentDescription = null,
                tint = Gray500,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onEvent(AnniversaryUiEvent.NextMonthClicked) }
                    .padding(6.dp)
            )
            Spacer(Modifier.weight(1f))
            CalendarTypeChip(
                text = "양력",
                selected = form.calendarType == AnniversaryCalendarType.SOLAR,
                onClick = { onEvent(AnniversaryUiEvent.CalendarTypeChanged(AnniversaryCalendarType.SOLAR)) }
            )
            Spacer(Modifier.width(6.dp))
            CalendarTypeChip(
                text = "음력",
                selected = form.calendarType == AnniversaryCalendarType.LUNAR,
                onClick = { onEvent(AnniversaryUiEvent.CalendarTypeChanged(AnniversaryCalendarType.LUNAR)) }
            )
        }
        Spacer(Modifier.height(16.dp))
        CalendarGrid(
            form = form,
            onDateSelected = { onEvent(AnniversaryUiEvent.DateSelected(it)) }
        )
    }
}

@Composable
private fun CalendarGrid(
    form: AnniversaryFormState,
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
                    val selected = form.date?.let {
                        it.year == form.visibleYear && it.month == form.visibleMonth && it.day == day
                    } == true
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(34.dp)
                            .clickable(enabled = day > 0) {
                                onDateSelected(
                                    AnniversaryDate(
                                        year = form.visibleYear,
                                        month = form.visibleMonth,
                                        day = day
                                    )
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(CircleShape)
                                .background(if (selected) Primary500 else Color.Transparent),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (day == 0) "" else day.toString(),
                                style = HaloType.body03Regular.copy(fontSize = 14.sp),
                                color = if (selected) White else Gray600
                            )
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
            style = HaloType.body02Medium.copy(fontSize = 15.sp),
            color = if (enabled) Gray600 else Gray400,
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
private fun AnniversaryDateField(
    form: AnniversaryFormState,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = Gray30
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = form.date?.formatted() ?: "날짜를 선택해주세요.",
                style = HaloType.body03Regular.copy(fontSize = 14.sp),
                color = if (form.date == null) Gray300 else Gray800,
                modifier = Modifier.weight(1f)
            )
            if (form.date != null) {
                Text(
                    text = form.calendarType.label,
                    style = HaloType.caption01Medium.copy(fontSize = 11.5.sp),
                    color = Gray300
                )
                Spacer(Modifier.width(12.dp))
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_home_right_arrow),
                contentDescription = null,
                tint = Gray400,
                modifier = Modifier
                    .size(18.dp)
                    .graphicsLayer(rotationZ = if (form.isCalendarExpanded) -90f else 90f)
            )
        }
    }
}

@Composable
private fun AnniversaryTextField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    minHeight: androidx.compose.ui.unit.Dp = 44.dp,
    singleLine: Boolean = true
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
                .padding(horizontal = 18.dp, vertical = if (singleLine) 0.dp else 16.dp),
            contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = singleLine,
                textStyle = HaloType.body03Regular.copy(
                    fontSize = 14.sp,
                    color = Gray800
                ).toTextStyle(),
                modifier = Modifier.fillMaxWidth()
            )
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = HaloType.body03Regular.copy(fontSize = 13.5.sp),
                    color = Gray300
                )
            }
        }
    }
}

@Composable
private fun ReadOnlyBox(
    text: String,
    trailing: String? = null,
    minHeight: androidx.compose.ui.unit.Dp = 50.dp,
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
            modifier = Modifier.padding(horizontal = 18.dp, vertical = if (alignTop) 18.dp else 0.dp),
            verticalAlignment = if (alignTop) Alignment.Top else Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = HaloType.body03Regular.copy(fontSize = 14.sp),
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
private fun AnniversarySectionTitle(text: String) {
    Text(
        text = text,
        style = HaloType.heading03SemiBold.copy(fontSize = 21.sp),
        color = Gray800
    )
}

@Composable
private fun AnniversaryInputLabel(text: String) {
    Text(
        text = text,
        style = HaloType.body02SemiBold.copy(fontSize = 16.sp),
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
        shape = RoundedCornerShape(20.dp),
        color = if (active) Primary50 else Gray30,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            style = HaloType.body03Medium.copy(fontSize = 13.sp),
            color = if (active) Primary600 else Gray500,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
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
        painter = painterResource(id = R.drawable.ic_home_right_arrow),
        contentDescription = null,
        tint = tint,
        modifier = Modifier.size(20.dp)
    )
}

private fun TextStyle.toTextStyle(): TextStyle = this
