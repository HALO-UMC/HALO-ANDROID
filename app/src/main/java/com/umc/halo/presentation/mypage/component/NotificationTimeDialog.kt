package com.umc.halo.presentation.mypage.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.umc.halo.R
import com.umc.halo.presentation.component.HaloNumberWheelField
import com.umc.halo.presentation.mypage.MyPageUiEvent
import com.umc.halo.presentation.mypage.MyPageUiState
import com.umc.halo.presentation.mypage.formattedNotificationTime
import com.umc.halo.presentation.theme.Gray300
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.Primary600
import com.umc.halo.presentation.theme.White

@Composable
fun NotificationTimeDialog(
    uiState: MyPageUiState,
    onEvent: (MyPageUiEvent) -> Unit
) {
    Dialog(
        onDismissRequest = {
            onEvent(MyPageUiEvent.NotificationTimeDismissed)
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 308.dp),
            shape = RoundedCornerShape(16.dp),
            color = White
        ) {
            Column(
                modifier = Modifier.padding(
                    start = 24.dp,
                    top = 24.dp,
                    end = 24.dp,
                    bottom = 24.dp
                )
            ) {
                DialogHeader(
                    timeText = uiState.formattedNotificationTime(),
                    showCurrentTime = uiState.isEditingNotificationTime,
                    onClose = {
                        onEvent(MyPageUiEvent.NotificationTimeDismissed)
                    }
                )

                Spacer(Modifier.height(18.dp))

                if (uiState.isEditingNotificationTime) {
                    NotificationTimeEditor(
                        hour = uiState.draftNotificationHour,
                        minute = uiState.draftNotificationMinute,
                        onHourChange = {
                            onEvent(MyPageUiEvent.NotificationHourChanged(it))
                        },
                        onMinuteChange = {
                            onEvent(MyPageUiEvent.NotificationMinuteChanged(it))
                        }
                    )
                } else {
                    NotificationTimeSummary(
                        timeText = uiState.formattedNotificationTime(),
                        onEditClick = {
                            onEvent(MyPageUiEvent.NotificationTimeEditClicked)
                        }
                    )
                }

                Spacer(Modifier.height(48.dp))

                DialogPrimaryButton(
                    text = "완료",
                    onClick = {
                        onEvent(MyPageUiEvent.NotificationTimeConfirmed)
                    }
                )
            }
        }
    }
}

@Composable
private fun DialogHeader(
    timeText: String,
    showCurrentTime: Boolean,
    onClose: () -> Unit
) {
    Row(verticalAlignment = Alignment.Top) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "설정한 시간에 알림을 보내드려요!",
                style = HaloType.body01SemiBold,
                color = Gray800
            )
            if (showCurrentTime) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "현재 알림 발송 시각 : $timeText",
                    style = HaloType.body03Regular,
                    color = Primary600
                )
            }
        }

        IconButton(
            onClick = onClose,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_common_close),
                contentDescription = "닫기",
                tint = Gray400
            )
        }
    }
}

@Composable
private fun NotificationTimeEditor(
    hour: Int,
    minute: Int,
    onHourChange: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit
) {
    Column {
        Text(
            text = "설정 시간",
            style = HaloType.body02SemiBold,
            color = Gray800
        )
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            TimeWheelBox(
                selectedValue = hour,
                values = 0..23,
                label = "시",
                onValueChange = onHourChange
            )
            TimeWheelBox(
                selectedValue = minute,
                values = 0..59,
                label = "분",
                onValueChange = onMinuteChange
            )
        }
    }
}

@Composable
private fun NotificationTimeSummary(
    timeText: String,
    onEditClick: () -> Unit
) {
    Column {
        Text(
            text = "설정 시간",
            style = HaloType.body03Regular,
            color = Gray800
        )
        Spacer(Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = timeText,
                style = HaloType.body01SemiBold,
                color = Primary600,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = onEditClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_mypage_edit_pencil),
                    contentDescription = "시간 수정",
                    tint = Gray300
                )
            }
        }
    }
}

@Composable
private fun TimeWheelBox(
    selectedValue: Int,
    values: IntRange,
    label: String,
    onValueChange: (Int) -> Unit
) {
    HaloNumberWheelField(
        selectedValue = selectedValue,
        values = values.toList(),
        placeholder = "00",
        unit = label,
        onValueSelected = onValueChange,
        modifier = Modifier
            .width(100.dp)
            .height(58.dp),
        valueFormatter = { it.toString().padStart(2, '0') },
        fieldHeight = 58.dp,
        cornerRadius = 18.dp,
        usePlaceholder = false,
        circular = true
    )
}

@Composable
private fun DialogPrimaryButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp),
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Primary500,
            contentColor = White
        )
    ) {
        Text(
            text = text,
            style = HaloType.body02SemiBold
        )
    }
}
