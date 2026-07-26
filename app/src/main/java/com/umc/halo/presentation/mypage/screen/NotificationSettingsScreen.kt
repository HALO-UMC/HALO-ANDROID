package com.umc.halo.presentation.mypage.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.halo.presentation.mypage.MyPageUiEvent
import com.umc.halo.presentation.mypage.MyPageUiState
import com.umc.halo.presentation.mypage.component.MyPageContainer
import com.umc.halo.presentation.mypage.component.MyPageTopBar
import com.umc.halo.presentation.mypage.component.NotificationTimeDialog
import com.umc.halo.presentation.mypage.component.SettingSwitchRow
import com.umc.halo.presentation.mypage.component.TimeSettingCard
import com.umc.halo.presentation.mypage.formattedNotificationTime
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType

@Composable
fun NotificationSettingsScreen(
    uiState: MyPageUiState,
    onEvent: (MyPageUiEvent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.showNotificationTimeDialog) {
        NotificationTimeDialog(
            uiState = uiState,
            onEvent = onEvent
        )
    }

    MyPageContainer(modifier = modifier) {
        MyPageTopBar(title = "알림 설정", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 34.dp)
        ) {
            SettingSwitchRow(
                title = "전체 알림 설정",
                checked = uiState.allNotificationsEnabled,
                onCheckedChange = {
                    onEvent(MyPageUiEvent.AllNotificationsChanged(it))
                }
            )
            HorizontalDivider(
                color = Gray100,
                modifier = Modifier.padding(top = 20.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onEvent(MyPageUiEvent.NotificationTimeClicked)
                    }
                    .padding(top = 20.dp)
            ) {
                Text(
                    text = "정기 알림 시간 설정",
                    style = HaloType.body02SemiBold.copy(fontSize = 15.sp),
                    color = Gray800
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "원하는 시간에 알림을 발송해드려요!",
                    style = HaloType.caption01Medium.copy(fontSize = 10.5.sp),
                    color = Gray500
                )
                Spacer(Modifier.height(14.dp))
                TimeSettingCard(
                    timeText = "현재 알림 발송 시각 : ${uiState.formattedNotificationTime()}",
                    onClick = {
                        onEvent(MyPageUiEvent.NotificationTimeClicked)
                    }
                )
            }

            HorizontalDivider(
                color = Gray100,
                modifier = Modifier.padding(vertical = 22.dp)
            )

            SettingSwitchRow(
                title = "오늘의 장 알림",
                checked = uiState.todayChapterNotificationEnabled,
                onCheckedChange = {
                    onEvent(MyPageUiEvent.TodayChapterNotificationChanged(it))
                }
            )
            Spacer(Modifier.height(22.dp))
            SettingSwitchRow(
                title = "기념일 알림",
                checked = uiState.anniversaryNotificationEnabled,
                onCheckedChange = {
                    onEvent(MyPageUiEvent.AnniversaryNotificationChanged(it))
                }
            )
            Spacer(Modifier.height(22.dp))
            SettingSwitchRow(
                title = "리텐션 알림",
                checked = uiState.retentionNotificationEnabled,
                onCheckedChange = {
                    onEvent(MyPageUiEvent.RetentionNotificationChanged(it))
                }
            )

            Spacer(Modifier.weight(1f))
            HorizontalDivider(color = Gray100)
        }
    }
}
