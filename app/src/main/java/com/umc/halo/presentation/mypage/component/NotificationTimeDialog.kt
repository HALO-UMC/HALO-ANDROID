package com.umc.halo.presentation.mypage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.umc.halo.R
import com.umc.halo.presentation.mypage.MyPageUiEvent
import com.umc.halo.presentation.mypage.MyPageUiState
import com.umc.halo.presentation.mypage.formattedNotificationTime
import com.umc.halo.presentation.theme.Gray30
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.Gray700
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
            shape = RoundedCornerShape(14.dp),
            color = White
        ) {
            Column(
                modifier = Modifier.padding(
                    start = 24.dp,
                    top = 18.dp,
                    end = 24.dp,
                    bottom = 18.dp
                )
            ) {
                DialogHeader(
                    timeText = uiState.formattedNotificationTime(),
                    onClose = {
                        onEvent(MyPageUiEvent.NotificationTimeDismissed)
                    }
                )

                Spacer(Modifier.height(22.dp))

                if (uiState.isEditingNotificationTime) {
                    NotificationTimeEditor(
                        hour = uiState.notificationHour,
                        minute = uiState.notificationMinute,
                        onHourDecrease = {
                            onEvent(MyPageUiEvent.NotificationHourDecreased)
                        },
                        onHourIncrease = {
                            onEvent(MyPageUiEvent.NotificationHourIncreased)
                        },
                        onMinuteDecrease = {
                            onEvent(MyPageUiEvent.NotificationMinuteDecreased)
                        },
                        onMinuteIncrease = {
                            onEvent(MyPageUiEvent.NotificationMinuteIncreased)
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

                Spacer(Modifier.height(26.dp))

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
    onClose: () -> Unit
) {
    Row(verticalAlignment = Alignment.Top) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "설정한 시간에 알림을 보내드려요!",
                style = HaloType.body02SemiBold.copy(fontSize = 15.sp),
                color = Gray800
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "현재 알림 발송 시각 : $timeText",
                style = HaloType.caption01Medium.copy(fontSize = 11.5.sp),
                color = Primary500
            )
        }

        IconButton(
            onClick = onClose,
            modifier = Modifier.size(32.dp)
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
    onHourDecrease: () -> Unit,
    onHourIncrease: () -> Unit,
    onMinuteDecrease: () -> Unit,
    onMinuteIncrease: () -> Unit
) {
    Column {
        Text(
            text = "설정 시간",
            style = HaloType.caption01Medium.copy(fontSize = 10.5.sp),
            color = Gray700
        )
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            TimeStepperBox(
                value = hour.toString().padStart(2, '0'),
                label = "시",
                onDecrease = onHourDecrease,
                onIncrease = onHourIncrease,
                modifier = Modifier.weight(1f)
            )
            TimeStepperBox(
                value = minute.toString().padStart(2, '0'),
                label = "분",
                onDecrease = onMinuteDecrease,
                onIncrease = onMinuteIncrease,
                modifier = Modifier.weight(1f)
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
            style = HaloType.caption01Medium.copy(fontSize = 10.5.sp),
            color = Gray700
        )
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = timeText,
                style = HaloType.body01SemiBold.copy(fontSize = 17.sp),
                color = Primary600,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = onEditClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_mypage_edit),
                    contentDescription = "시간 수정",
                    tint = Gray400
                )
            }
        }
    }
}

@Composable
private fun TimeStepperBox(
    value: String,
    label: String,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(54.dp),
        shape = RoundedCornerShape(12.dp),
        color = Gray30
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Gray30)
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "-",
                style = HaloType.body02Medium.copy(fontSize = 15.sp),
                color = Gray500,
                modifier = Modifier.clickable(onClick = onDecrease)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = value,
                    style = HaloType.body02Medium.copy(fontSize = 15.sp),
                    color = Gray500
                )
                Spacer(Modifier.size(10.dp))
                Text(
                    text = label,
                    style = HaloType.body02Medium.copy(fontSize = 15.sp),
                    color = Gray700
                )
            }
            Text(
                text = "+",
                style = HaloType.body02Medium.copy(fontSize = 15.sp),
                color = Gray500,
                modifier = Modifier.clickable(onClick = onIncrease)
            )
        }
    }
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
            style = HaloType.body02SemiBold.copy(fontSize = 14.sp)
        )
    }
}
