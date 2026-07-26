package com.umc.halo.presentation.mypage.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.halo.presentation.mypage.MyPageUiEvent
import com.umc.halo.presentation.mypage.MyPageUiState
import com.umc.halo.presentation.mypage.component.MyPageContainer
import com.umc.halo.presentation.mypage.component.MyPageTopBar
import com.umc.halo.presentation.mypage.component.SettingSwitchRow
import com.umc.halo.presentation.mypage.component.SystemVolumeSlider
import com.umc.halo.presentation.mypage.component.TrackRow
import com.umc.halo.presentation.theme.Gray30
import com.umc.halo.presentation.theme.Gray700
import com.umc.halo.presentation.theme.HaloType

@Composable
fun SystemSettingsScreen(
    uiState: MyPageUiState,
    onEvent: (MyPageUiEvent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tracks = listOf("산들바람", "산들바람", "산들바람")

    MyPageContainer(modifier = modifier) {
        MyPageTopBar(title = "시스템 설정", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 34.dp)
        ) {
            SettingSwitchRow(
                title = "배경음악",
                checked = uiState.bgmEnabled,
                onCheckedChange = {
                    onEvent(MyPageUiEvent.BgmEnabledChanged(it))
                }
            )

            if (uiState.bgmEnabled) {
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
                            style = HaloType.body03Medium.copy(fontSize = 11.sp),
                            color = Gray700
                        )
                        Spacer(Modifier.height(11.dp))
                        SystemVolumeSlider(
                            value = uiState.volume,
                            onValueChange = {
                                onEvent(MyPageUiEvent.VolumeChanged(it))
                            }
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
                            style = HaloType.body03Medium.copy(fontSize = 11.sp),
                            color = Gray700,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                        Spacer(Modifier.height(14.dp))

                        tracks.forEachIndexed { index, track ->
                            TrackRow(
                                title = track,
                                selected = uiState.selectedTrackIndex == index,
                                playing = uiState.playingTrackIndex == index,
                                onClick = {
                                    onEvent(MyPageUiEvent.TrackClicked(index))
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
