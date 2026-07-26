package com.umc.halo.presentation.mypage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umc.halo.R
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.Gray900
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.White

@Composable
fun MyPageContainer(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxSize()
                .widthIn(max = 420.dp),
            content = content
        )
    }
}

@Composable
fun MyPageBrandTopBar() {
    Box(
        modifier = Modifier
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
fun MyPageTopBar(
    title: String,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
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
