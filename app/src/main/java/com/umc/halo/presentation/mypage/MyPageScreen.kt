package com.umc.halo.presentation.mypage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.halo.R
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray200
import com.umc.halo.presentation.theme.Gray300
import com.umc.halo.presentation.theme.Gray30
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.Gray600
import com.umc.halo.presentation.theme.Gray700
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.Gray900
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary100
import com.umc.halo.presentation.theme.Primary30
import com.umc.halo.presentation.theme.Primary400
import com.umc.halo.presentation.theme.Primary50
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.Primary600
import com.umc.halo.presentation.theme.White

@Composable
fun MyPageScreen(
    onNavigateToRelationshipInfo: () -> Unit,
    onNavigateToAnniversary: () -> Unit,
    onNavigateToSystemSettings: () -> Unit,
    onNavigateToNotificationSettings: () -> Unit,
    onNavigateToAccountManagement: () -> Unit,
    modifier: Modifier = Modifier
) {
    MyPageContainer(modifier = modifier) {
        MyPageBrandTopBar()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp)
        ) {
            SectionTitle("내 정보")
            Spacer(Modifier.height(14.dp))
            ProfileCard()

            Spacer(Modifier.height(32.dp))
            SectionTitle("추가 기능")
            Spacer(Modifier.height(14.dp))
            MenuRow(title = "관계 정보", onClick = onNavigateToRelationshipInfo)
            MenuRow(title = "기념일 관리", onClick = onNavigateToAnniversary)

            Spacer(Modifier.height(24.dp))
            SectionTitle("기본 설정")
            Spacer(Modifier.height(14.dp))
            MenuRow(title = "시스템 설정", onClick = onNavigateToSystemSettings)
            MenuRow(title = "알림 설정", onClick = onNavigateToNotificationSettings)
            MenuRow(title = "계정 관리", onClick = onNavigateToAccountManagement)
        }
    }
}

@Composable
fun SystemSettingsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var bgmEnabled by rememberSaveable { mutableStateOf(true) }
    var volume by rememberSaveable { mutableFloatStateOf(0.42f) }
    var selectedTrack by rememberSaveable { mutableIntStateOf(0) }
    var playingTrack by rememberSaveable { mutableIntStateOf(0) }
    val tracks = listOf("산들바람", "산들바람", "산들바람")

    MyPageContainer(modifier = modifier) {
        MyPageTopBar(title = "시스템 설정", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 40.dp)
        ) {
            SettingSwitchRow(
                title = "배경음악",
                checked = bgmEnabled,
                onCheckedChange = { bgmEnabled = it }
            )

            if (bgmEnabled) {
                Spacer(Modifier.height(28.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Gray30
                ) {
                    Column(
                        modifier = Modifier.padding(
                            horizontal = 20.dp,
                            vertical = 18.dp
                        )
                    ) {
                        Text(
                            text = "음량",
                            style = HaloType.body03Medium,
                            color = Gray700
                        )
                        Slider(
                            value = volume,
                            onValueChange = { volume = it },
                            colors = SliderDefaults.colors(
                                thumbColor = Gray600,
                                activeTrackColor = Gray600,
                                inactiveTrackColor = Gray200
                            )
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Gray30
                ) {
                    Column(
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 18.dp
                        )
                    ) {
                        Text(
                            text = "재생 목록",
                            style = HaloType.body03Medium,
                            color = Gray700,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                        Spacer(Modifier.height(14.dp))

                        tracks.forEachIndexed { index, track ->
                            TrackRow(
                                title = track,
                                selected = selectedTrack == index,
                                playing = playingTrack == index,
                                onClick = {
                                    selectedTrack = index
                                    playingTrack = index
                                }
                            )
                            if (index < tracks.lastIndex) {
                                Spacer(Modifier.height(12.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationSettingsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var allEnabled by rememberSaveable { mutableStateOf(true) }
    var regularEnabled by rememberSaveable { mutableStateOf(true) }
    var todayEnabled by rememberSaveable { mutableStateOf(true) }
    var anniversaryEnabled by rememberSaveable { mutableStateOf(true) }
    var retentionEnabled by rememberSaveable { mutableStateOf(true) }
    var showTimeDialog by remember { mutableStateOf(false) }
    var hour by rememberSaveable { mutableIntStateOf(9) }
    var minute by rememberSaveable { mutableIntStateOf(0) }
    var isPm by rememberSaveable { mutableStateOf(false) }

    if (showTimeDialog) {
        NotificationTimeDialog(
            initialHour = hour,
            initialMinute = minute,
            initialIsPm = isPm,
            onDismiss = { showTimeDialog = false },
            onConfirm = { selectedHour, selectedMinute, selectedIsPm ->
                hour = selectedHour
                minute = selectedMinute
                isPm = selectedIsPm
                showTimeDialog = false
            }
        )
    }

    MyPageContainer(modifier = modifier) {
        MyPageTopBar(title = "알림 설정", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 36.dp)
        ) {
            SettingSwitchRow(
                title = "전체 알림 설정",
                checked = allEnabled,
                onCheckedChange = {
                    allEnabled = it
                    regularEnabled = it
                    todayEnabled = it
                    anniversaryEnabled = it
                    retentionEnabled = it
                }
            )
            Divider(color = Gray100, modifier = Modifier.padding(top = 20.dp))

            Spacer(Modifier.height(20.dp))
            SettingSwitchRow(
                title = "정기 알림 시간 설정",
                description = "원하는 시간에 알림을 발송해드려요!",
                checked = regularEnabled,
                onCheckedChange = { regularEnabled = it }
            )
            if (regularEnabled) {
                Spacer(Modifier.height(14.dp))
                TimeSettingCard(
                    timeText = "현재 알림 발송 시각 : ${formatKoreanTime(hour, minute, isPm)}",
                    onClick = { showTimeDialog = true }
                )
            }

            Divider(color = Gray100, modifier = Modifier.padding(vertical = 22.dp))

            SettingSwitchRow(
                title = "오늘의 장 알림",
                checked = todayEnabled,
                onCheckedChange = { todayEnabled = it }
            )
            Spacer(Modifier.height(22.dp))
            SettingSwitchRow(
                title = "기념일 알림",
                checked = anniversaryEnabled,
                onCheckedChange = { anniversaryEnabled = it }
            )
            Spacer(Modifier.height(22.dp))
            SettingSwitchRow(
                title = "리텐션 알림",
                checked = retentionEnabled,
                onCheckedChange = { retentionEnabled = it }
            )

            Spacer(Modifier.weight(1f))
            Divider(color = Gray100)
        }
    }
}

@Composable
fun AccountManagementScreen(
    onBack: () -> Unit,
    onNavigateToAccountInfo: () -> Unit,
    onNavigateToOpenLicense: () -> Unit,
    onNavigateToPrivacyPolicy: () -> Unit,
    onNavigateToTerms: () -> Unit,
    modifier: Modifier = Modifier
) {
    MyPageContainer(modifier = modifier) {
        MyPageTopBar(title = "계정 관리", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 44.dp)
        ) {
            MenuRow(title = "계정 정보", onClick = onNavigateToAccountInfo)
            MenuRow(title = "오픈 라이선스", onClick = onNavigateToOpenLicense)
            MenuRow(title = "개인정보 처리방침", onClick = onNavigateToPrivacyPolicy)
            MenuRow(title = "이용 약관", onClick = onNavigateToTerms)
        }
    }
}

@Composable
fun AccountInfoScreen(
    onBack: () -> Unit,
    onNavigateToWithdraw: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        ConfirmActionDialog(
            title = "정말 로그아웃하시겠습니까?",
            description = "현재 계정에서 로그아웃 되며 재로그인이 필요해요.",
            buttonText = "로그아웃 하기",
            onDismiss = { showLogoutDialog = false },
            onConfirm = onNavigateToLogin
        )
    }

    MyPageContainer(modifier = modifier) {
        MyPageTopBar(title = "계정 정보", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 36.dp, bottom = 32.dp)
        ) {
            SectionTitle("계정 정보")
            Spacer(Modifier.height(20.dp))
            InfoRow(label = "닉네임", value = "난혁")
            InfoRow(label = "로그인 방식", value = "카카오 로그인")
            InfoRow(label = "이메일", value = "kimjooyeon038@gmail.com")
            InfoRow(label = "계정 생성일", value = "2026.06.28")

            Spacer(Modifier.height(40.dp))
            SectionTitle("서비스 이용 정보")
            Spacer(Modifier.height(20.dp))
            InfoRow(label = "알림 상태", value = "수신 중")
            MenuRow(title = "로그 아웃", onClick = { showLogoutDialog = true })
            MenuRow(title = "회원 탈퇴", onClick = onNavigateToWithdraw)
        }
    }
}

@Composable
fun WithdrawScreen(
    onBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showConfirmDialog by remember { mutableStateOf(false) }

    if (showConfirmDialog) {
        ConfirmActionDialog(
            title = "정말 탈퇴하시겠어요?",
            description = "탈퇴 시 모든 정보가 사라집니다.",
            buttonText = "탈퇴하기",
            onDismiss = { showConfirmDialog = false },
            onConfirm = onNavigateToLogin
        )
    }

    MyPageContainer(modifier = modifier) {
        MyPageTopBar(title = "회원 탈퇴", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 40.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "정말 탈퇴하시겠어요?",
                style = HaloType.heading02SemiBold,
                color = Gray800,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "회원 탈퇴 시 계정 정보와 진행 중인 스토리북, 저장된 모든 기록이 삭제되며, 삭제된 데이터는 복구할 수 없어요.",
                style = HaloType.body03Regular.copy(lineHeight = 17.sp),
                color = Gray600,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(36.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_orange_character),
                contentDescription = null,
                modifier = Modifier.size(width = 104.dp, height = 116.dp)
            )

            Spacer(Modifier.height(28.dp))
            WarningLine("계정 정보가 삭제돼요.")
            WarningLine("기록과 스토리북이 모두 사라져요")
            WarningLine("삭제 후에는 복구할 수 없어요.")

            Spacer(Modifier.weight(1f))
            PrimaryActionButton(
                text = "탈퇴 할게요",
                onClick = { showConfirmDialog = true }
            )
            Spacer(Modifier.height(12.dp))
            SecondaryActionButton(text = "취소", onClick = onBack)
        }
    }
}

@Composable
fun TermsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    MyPageContainer(modifier = modifier) {
        MyPageTopBar(title = "이용 약관", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 28.dp)
        ) {
            Text(
                text = "최종 업데이트 | 26. 06.26",
                style = HaloType.caption01Medium,
                color = Gray700
            )
            Spacer(Modifier.height(28.dp))
            SectionTitle("세부 약관")
            Spacer(Modifier.height(18.dp))
            repeat(4) {
                MenuRow(
                    title = "개인정보 동의",
                    titleColor = Gray500,
                    onClick = {}
                )
            }
        }
    }
}

@Composable
fun OpenLicenseScreen(
    onBack: () -> Unit,
    onNavigateToAccountInfo: () -> Unit,
    onNavigateToPrivacyPolicy: () -> Unit,
    onNavigateToTerms: () -> Unit,
    modifier: Modifier = Modifier
) {
    MyPageContainer(modifier = modifier) {
        MyPageTopBar(title = "오픈라이선스", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 28.dp)
        ) {
            Text(
                text = "항목을 누르면 전문을 볼 수 있어요.",
                style = HaloType.caption01Medium,
                color = Gray700
            )
            Spacer(Modifier.height(28.dp))
            MenuRow(title = "계정 정보", onClick = onNavigateToAccountInfo)
            MenuRow(title = "오픈 라이선스", onClick = {})
            MenuRow(title = "개인정보 처리방침", onClick = onNavigateToPrivacyPolicy)
            MenuRow(title = "이용 약관", onClick = onNavigateToTerms)
        }
    }
}

@Composable
fun PrivacyPolicyScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    MyPageContainer(modifier = modifier) {
        MyPageTopBar(title = "개인정보 처리방침", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 28.dp)
        ) {
            Text(
                text = "최종 업데이트 | 26. 06.26",
                style = HaloType.caption01Medium,
                color = Gray700
            )
            Spacer(Modifier.height(28.dp))
            SectionTitle("세부 약관")
            Spacer(Modifier.height(18.dp))
            repeat(4) {
                MenuRow(
                    title = "개인정보 동의",
                    titleColor = Gray500,
                    onClick = {}
                )
            }
        }
    }
}

@Composable
fun RelationshipInfoScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    MyPageContainer(modifier = modifier) {
        MyPageTopBar(title = "관계 정보", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 26.dp, bottom = 32.dp)
        ) {
            Text(
                text = "부모님의 성격/성향",
                style = HaloType.body02SemiBold,
                color = Gray800
            )
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TagChip("낙천적인")
                TagChip("온화한")
                TagChip("사교적인")
            }

            Divider(color = Gray100, modifier = Modifier.padding(vertical = 22.dp))

            Text(
                text = "부모님과 나의 관계는?",
                style = HaloType.body02SemiBold,
                color = Gray800
            )
            Spacer(Modifier.height(12.dp))
            RelationshipAnswerCard(
                title = "대체로 좋은 편이에요",
                description = "일상적인 안부를 나누며 서로를 존중해요"
            )

            Divider(color = Gray100, modifier = Modifier.padding(vertical = 22.dp))

            Text(
                text = "어떤 사이가 되고 싶나요?",
                style = HaloType.body02SemiBold,
                color = Gray800
            )
            Spacer(Modifier.height(12.dp))
            RelationshipAnswerCard(
                title = "같이 보내는 시간을 만들고 싶어요",
                description = null
            )
        }
    }
}

@Composable
fun AnniversaryPlaceholderScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    MyPageContainer(modifier = modifier) {
        MyPageTopBar(title = "기념일 관리", onBack = onBack)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "기념일 관리 화면은 다음 단계에서 이어서 구현할게요.",
                style = HaloType.body02Medium,
                color = Gray500,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun MyPageContainer(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .widthIn(max = 420.dp)
            .navigationBarsPadding(),
        content = content
    )
}

@Composable
private fun MyPageBrandTopBar() {
    Box(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .height(56.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "HALO",
            style = HaloType.body01SemiBold,
            color = Gray900
        )
    }
}

@Composable
private fun MyPageTopBar(
    title: String,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .height(56.dp)
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 8.dp)
                .size(44.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_common_backward_arrow),
                contentDescription = "뒤로가기",
                tint = Gray800
            )
        }
        Text(
            text = title,
            style = HaloType.body01SemiBold,
            color = Gray900,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun ProfileCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Gray30
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = White,
                modifier = Modifier.size(58.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_orange_character),
                    contentDescription = null,
                    modifier = Modifier.padding(9.dp)
                )
            }
            Spacer(Modifier.width(18.dp))
            Column {
                Text(
                    text = "주현AB",
                    style = HaloType.heading03SemiBold,
                    color = Gray800
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "2003.09.25",
                    style = HaloType.body02Regular,
                    color = Gray500
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = HaloType.body01SemiBold,
        color = Gray800
    )
}

@Composable
private fun MenuRow(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    titleColor: Color = Gray800
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = HaloType.body02Medium,
            color = titleColor,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_home_right_arrow),
            contentDescription = null,
            tint = Gray700,
            modifier = Modifier.size(18.dp)
        )
    }
    Divider(color = Gray100)
}

@Composable
private fun SettingSwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = HaloType.body02SemiBold,
                color = Gray800
            )
            if (description != null) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = description,
                    style = HaloType.caption01Medium,
                    color = Gray500
                )
            }
        }
        HaloSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun HaloSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        colors = SwitchDefaults.colors(
            checkedThumbColor = White,
            checkedTrackColor = Gray600,
            uncheckedThumbColor = Gray300,
            uncheckedTrackColor = Gray100,
            uncheckedBorderColor = Color.Transparent,
            checkedBorderColor = Color.Transparent
        )
    )
}

@Composable
private fun TrackRow(
    title: String,
    selected: Boolean,
    playing: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = White
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Primary100)
            )
            Spacer(Modifier.width(14.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        append(title)
                    }
                    append("  ")
                    withStyle(
                        SpanStyle(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    ) {
                        append("Track 01")
                    }
                },
                style = HaloType.body02Medium,
                color = if (selected) Primary600 else Gray700,
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = painterResource(
                    id = if (playing) {
                        R.drawable.ic_home_bgmplayer_pause
                    } else {
                        R.drawable.ic_home_bgmplayer_play
                    }
                ),
                contentDescription = null,
                tint = if (selected) Primary600 else Gray800,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

@Composable
private fun TimeSettingCard(
    timeText: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Primary30)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = timeText,
            style = HaloType.body03Medium,
            color = Primary600,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_home_right_arrow),
            contentDescription = null,
            tint = Primary600,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun NotificationTimeDialog(
    initialHour: Int,
    initialMinute: Int,
    initialIsPm: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int, Boolean) -> Unit
) {
    var hour by rememberSaveable { mutableIntStateOf(initialHour) }
    var minute by rememberSaveable { mutableIntStateOf(initialMinute) }
    var isPm by rememberSaveable { mutableStateOf(initialIsPm) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        containerColor = White,
        shape = RoundedCornerShape(14.dp),
        text = {
            Column {
                Row(verticalAlignment = Alignment.Top) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "설정한 시간에 알림을 보내드려요!",
                            style = HaloType.body02SemiBold,
                            color = Gray800
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "현재 알림 발송 시각 : ${formatKoreanTime(initialHour, initialMinute, initialIsPm)}",
                            style = HaloType.caption01Medium,
                            color = Primary500
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_common_close),
                            contentDescription = "닫기",
                            tint = Gray400
                        )
                    }
                }

                Spacer(Modifier.height(22.dp))
                Text(
                    text = "설정 시간",
                    style = HaloType.caption01Medium,
                    color = Gray700
                )
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TimePeriodChip(
                        text = "오전",
                        selected = !isPm,
                        onClick = { isPm = false }
                    )
                    TimePeriodChip(
                        text = "오후",
                        selected = isPm,
                        onClick = { isPm = true }
                    )
                }

                Spacer(Modifier.height(14.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    TimeStepper(
                        value = hour,
                        label = "시",
                        onMinus = { hour = if (hour == 1) 12 else hour - 1 },
                        onPlus = { hour = if (hour == 12) 1 else hour + 1 },
                        modifier = Modifier.weight(1f)
                    )
                    TimeStepper(
                        value = minute,
                        label = "분",
                        onMinus = { minute = (minute + 55) % 60 },
                        onPlus = { minute = (minute + 5) % 60 },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(22.dp))
                PrimaryActionButton(
                    text = "완료",
                    onClick = { onConfirm(hour, minute, isPm) }
                )
            }
        }
    )
}

@Composable
private fun TimePeriodChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .height(34.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (selected) Primary50 else Gray30,
        border = if (selected) BorderStroke(1.dp, Primary500) else null
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 18.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = HaloType.body03Medium,
                color = if (selected) Primary600 else Gray500
            )
        }
    }
}

@Composable
private fun TimeStepper(
    value: Int,
    label: String,
    onMinus: () -> Unit,
    onPlus: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(50.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Gray30)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "-",
            style = HaloType.body01SemiBold,
            color = Gray500,
            modifier = Modifier.clickable(onClick = onMinus)
        )
        Text(
            text = "${value.toString().padStart(2, '0')}   $label",
            style = HaloType.body02Medium,
            color = Gray700
        )
        Text(
            text = "+",
            style = HaloType.body01SemiBold,
            color = Gray500,
            modifier = Modifier.clickable(onClick = onPlus)
        )
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = HaloType.body02Medium,
            color = Gray800,
            modifier = Modifier.weight(0.9f)
        )
        Text(
            text = value,
            style = HaloType.body03Regular,
            color = Gray700,
            textAlign = TextAlign.End,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1.1f)
        )
    }
    Divider(color = Gray100)
}

@Composable
private fun ConfirmActionDialog(
    title: String,
    description: String,
    buttonText: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        containerColor = White,
        shape = RoundedCornerShape(14.dp),
        text = {
            Column {
                Row(verticalAlignment = Alignment.Top) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            style = HaloType.body02SemiBold,
                            color = Gray800
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = description,
                            style = HaloType.caption01Medium,
                            color = Gray600
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_common_close),
                            contentDescription = "닫기",
                            tint = Gray400
                        )
                    }
                }
                Spacer(Modifier.height(26.dp))
                PrimaryActionButton(text = buttonText, onClick = onConfirm)
            }
        }
    )
}

@Composable
private fun PrimaryActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Primary500,
            contentColor = White
        )
    ) {
        Text(text = text, style = HaloType.body02SemiBold)
    }
}

@Composable
private fun SecondaryActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Gray100,
            contentColor = Gray400
        )
    ) {
        Text(text = text, style = HaloType.body02SemiBold)
    }
}

@Composable
private fun WarningLine(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = Gray500,
            modifier = Modifier.size(12.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "!",
                    style = HaloType.caption02Medium,
                    color = White
                )
            }
        }
        Spacer(Modifier.width(6.dp))
        Text(
            text = text,
            style = HaloType.body03Regular,
            color = Gray600
        )
    }
}

@Composable
private fun TagChip(text: String) {
    Surface(
        shape = RoundedCornerShape(100.dp),
        color = Primary50
    ) {
        Text(
            text = text,
            style = HaloType.caption01Medium,
            color = Primary500,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun RelationshipAnswerCard(
    title: String,
    description: String?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Primary50)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 13.dp)
        ) {
            Text(
                text = title,
                style = HaloType.body02SemiBold,
                color = Primary600
            )
            if (description != null) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = description,
                    style = HaloType.caption01Medium,
                    color = Primary400
                )
            }
        }
    }
}

private fun formatKoreanTime(
    hour: Int,
    minute: Int,
    isPm: Boolean
): String {
    val period = if (isPm) "오후" else "오전"
    val minuteText = if (minute == 0) "" else " ${minute.toString().padStart(2, '0')}분"
    return "$period ${hour.toString().padStart(2, '0')}시$minuteText"
}
