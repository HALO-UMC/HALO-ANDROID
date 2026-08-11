package com.umc.halo.presentation.storybook.chapter.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.umc.halo.presentation.theme.Gray700
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary500

/**
 * 조회 실패 + 보여줄 목록도 없는 상태
 * TODO: 문구·버튼 모양은 디자인 확정 후 교체
 *
 * @param text ${text}를 불러오지 못했어요
 */
@Composable
fun HaloLoadFailed(
    text: String,
    onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "${text}을 불러오지 못했어요.",
            style = HaloType.body02Regular,
            color = Gray700,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "다시 시도",
            style = HaloType.body02Medium,
            color = Primary500,
            modifier = Modifier
                .clip(RoundedCornerShape(100.dp))
                .clickable { onRetry() }
                .padding(horizontal = 20.dp, vertical = 8.dp)
        )
    }
}