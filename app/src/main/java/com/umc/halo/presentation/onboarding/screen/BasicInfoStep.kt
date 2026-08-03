package com.umc.halo.presentation.onboarding.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.umc.halo.presentation.onboarding.Gender
import com.umc.halo.presentation.onboarding.OnboardingUiEvent
import com.umc.halo.presentation.onboarding.OnboardingUiState
import com.umc.halo.presentation.onboarding.component.OnboardingBackButton
import com.umc.halo.presentation.onboarding.component.OnboardingBottomButton
import com.umc.halo.presentation.theme.Gray300
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.Gray30
import com.umc.halo.presentation.theme.Gray50
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary50
import com.umc.halo.presentation.theme.Primary600
import java.util.Calendar

@Composable
fun BasicInfoStep(
    uiState: OnboardingUiState,
    onEvent: (OnboardingUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val today = remember {
        Calendar.getInstance()
    }

    val currentYear = today.get(Calendar.YEAR)
    val currentMonth = today.get(Calendar.MONTH) + 1
    val currentDay = today.get(Calendar.DAY_OF_MONTH)

    val yearOptions = remember(currentYear) {
        (currentYear downTo MIN_BIRTH_YEAR).toList()
    }

    val monthOptions = remember(
        uiState.birthYear,
        currentYear,
        currentMonth
    ) {
        if (uiState.birthYear == currentYear) {
            (1..currentMonth).toList()
        } else {
            (1..12).toList()
        }
    }

    val maximumDay = remember(
        uiState.birthYear,
        uiState.birthMonth,
        currentYear,
        currentMonth,
        currentDay
    ) {
        calculateMaximumSelectableDay(
            selectedYear = uiState.birthYear,
            selectedMonth = uiState.birthMonth,
            currentYear = currentYear,
            currentMonth = currentMonth,
            currentDay = currentDay
        )
    }

    val dayOptions = remember(maximumDay) {
        (1..maximumDay).toList()
    }

    /*
     * 기존에 선택한 월이 현재 선택 가능한 월 범위를 벗어나면
     * 가장 마지막으로 선택 가능한 월로 보정한다.
     */
    LaunchedEffect(
        uiState.birthYear,
        monthOptions
    ) {
        val selectedMonth = uiState.birthMonth
        val maximumMonth = monthOptions.lastOrNull()

        if (
            selectedMonth != null &&
            maximumMonth != null &&
            selectedMonth > maximumMonth
        ) {
            onEvent(
                OnboardingUiEvent.BirthMonthSelected(maximumMonth)
            )
        }
    }

    /*
     * 연도나 월이 변경되어 기존 일자가 존재하지 않게 된 경우
     * 해당 월의 마지막 날짜로 자동 보정한다.
     */
    LaunchedEffect(
        uiState.birthYear,
        uiState.birthMonth,
        maximumDay
    ) {
        val selectedDay = uiState.birthDay

        if (
            selectedDay != null &&
            selectedDay > maximumDay
        ) {
            onEvent(
                OnboardingUiEvent.BirthDaySelected(maximumDay)
            )
        }
    }

    // 시스템 뒤로가기 버튼을 누르면 이름 입력 화면으로 돌아간다.
    BackHandler {
        onEvent(OnboardingUiEvent.BackClicked)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OnboardingBackButton(
            onClick = {
                onEvent(OnboardingUiEvent.BackClicked)
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(
                    start = 8.dp,
                    top = 14.dp
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(
                    top = 95.dp,
                    start = 24.dp,
                    end = 24.dp
                )
        ) {
            OnboardingQuestionTitle(
                stepNumber = "01",
                userName = uiState.userName,
                keyword = "성별"
            )

            Spacer(modifier = Modifier.height(30.dp))

            GenderSelectionRow(
                selectedGender = uiState.selectedGender,
                onGenderClick = { gender ->
                    onEvent(
                        OnboardingUiEvent.GenderSelected(gender)
                    )
                }
            )

            Spacer(modifier = Modifier.height(61.dp))

            OnboardingQuestionTitle(
                stepNumber = "02",
                userName = uiState.userName,
                keyword = "생년월일"
            )

            Spacer(modifier = Modifier.height(28.dp))

            BirthDateSelectionRow(
                selectedYear = uiState.birthYear,
                selectedMonth = uiState.birthMonth,
                selectedDay = uiState.birthDay,
                yearOptions = yearOptions,
                monthOptions = monthOptions,
                dayOptions = dayOptions,
                onYearSelected = { year ->
                    onEvent(
                        OnboardingUiEvent.BirthYearSelected(year)
                    )
                },
                onMonthSelected = { month ->
                    onEvent(
                        OnboardingUiEvent.BirthMonthSelected(month)
                    )
                },
                onDaySelected = { day ->
                    onEvent(
                        OnboardingUiEvent.BirthDaySelected(day)
                    )
                }
            )
        }

        OnboardingBottomButton(
            text = "다음",
            enabled = uiState.isNextEnabled,
            onClick = {
                onEvent(OnboardingUiEvent.NextClicked)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 20.dp
                )
        )
    }
}

@Composable
private fun OnboardingQuestionTitle(
    stepNumber: String,
    userName: String,
    keyword: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stepNumber,
            style = HaloType.body01Medium,
            color = Gray800
        )

        val questionText = buildAnnotatedString {
            append("${userName}님의 ")

            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.SemiBold
                )
            ) {
                append(keyword)
            }

            append("을 알려주세요.")
        }

        Text(
            text = questionText,
            style = HaloType.heading02Regular,
            color = Gray800
        )
    }
}

@Composable
private fun GenderSelectionRow(
    selectedGender: Gender?,
    onGenderClick: (Gender) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectableGroup()
    ) {
        GenderSelectionButton(
            text = Gender.MALE.label,
            selected = selectedGender == Gender.MALE,
            onClick = {
                onGenderClick(Gender.MALE)
            },
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        GenderSelectionButton(
            text = Gender.FEMALE.label,
            selected = selectedGender == Gender.FEMALE,
            onClick = {
                onGenderClick(Gender.FEMALE)
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun GenderSelectionButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(68.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                color = if (selected) {
                    Primary50
                } else {
                    Gray50
                }
            )
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = if (selected) {
                HaloType.body01SemiBold
            } else {
                HaloType.body01Medium
            },
            color = if (selected) {
                Primary600
            } else {
                Gray500
            }
        )
    }
}

@Composable
private fun BirthDateSelectionRow(
    selectedYear: Int?,
    selectedMonth: Int?,
    selectedDay: Int?,
    yearOptions: List<Int>,
    monthOptions: List<Int>,
    dayOptions: List<Int>,
    onYearSelected: (Int) -> Unit,
    onMonthSelected: (Int) -> Unit,
    onDaySelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BirthDateDropdownField(
            value = selectedYear,
            placeholder = "0000",
            unit = "년",
            options = yearOptions,
            fieldWidth = 116.dp,
            onValueSelected = onYearSelected,
            modifier = Modifier.weight(116f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        BirthDateDropdownField(
            value = selectedMonth,
            placeholder = "0",
            unit = "월",
            options = monthOptions,
            fieldWidth = 90.dp,
            onValueSelected = onMonthSelected,
            modifier = Modifier.weight(90f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        BirthDateDropdownField(
            value = selectedDay,
            placeholder = "0",
            unit = "일",
            options = dayOptions,
            fieldWidth = 90.dp,
            onValueSelected = onDaySelected,
            modifier = Modifier.weight(90f)
        )
    }
}

@Composable
private fun BirthDateDropdownField(
    value: Int?,
    placeholder: String,
    unit: String,
    options: List<Int>,
    fieldWidth: Dp,
    onValueSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Gray30)
                .clickable(
                    role = Role.Button,
                    onClick = {
                        isExpanded = true
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when {
                        value == null -> placeholder
                        unit == "년" -> value
                            .toString()
                            .padStart(4, '0')

                        else -> value.toString()
                    },
                    style = HaloType.body02Medium,
                    color = if (value == null) {
                        Gray300
                    } else {
                        Gray800
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = unit,
                    style = HaloType.body02Medium,
                    color = Gray800
                )
            }
        }

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
            },
            modifier = Modifier
                .width(fieldWidth)
                .heightIn(max = 280.dp)
        ) {
            options.forEach { option ->
                androidx.compose.material3.DropdownMenuItem(
                    text = {
                        Text(
                            text = "$option$unit",
                            style = HaloType.body02Medium,
                            color = Gray800
                        )
                    },
                    onClick = {
                        onValueSelected(option)
                        isExpanded = false
                    }
                )
            }
        }
    }
}

private fun calculateMaximumSelectableDay(
    selectedYear: Int?,
    selectedMonth: Int?,
    currentYear: Int,
    currentMonth: Int,
    currentDay: Int
): Int {
    if (
        selectedYear == null ||
        selectedMonth == null
    ) {
        return 31
    }

    val daysInSelectedMonth = daysInMonth(
        year = selectedYear,
        month = selectedMonth
    )

    return if (
        selectedYear == currentYear &&
        selectedMonth == currentMonth
    ) {
        minOf(
            daysInSelectedMonth,
            currentDay
        )
    } else {
        daysInSelectedMonth
    }
}

private fun daysInMonth(
    year: Int,
    month: Int
): Int {
    return when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30

        2 -> {
            if (isLeapYear(year)) {
                29
            } else {
                28
            }
        }

        else -> 31
    }
}

private fun isLeapYear(year: Int): Boolean {
    return year % 400 == 0 ||
            year % 4 == 0 &&
            year % 100 != 0
}

private const val MIN_BIRTH_YEAR = 1900
