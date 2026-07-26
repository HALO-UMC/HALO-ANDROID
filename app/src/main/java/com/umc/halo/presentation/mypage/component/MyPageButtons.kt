package com.umc.halo.presentation.mypage.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.White

@Composable
fun PrimaryActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 54.dp
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Primary500,
            contentColor = White
        )
    ) {
        Text(
            text = text,
            style = HaloType.body02SemiBold.copy(fontSize = 15.sp)
        )
    }
}

@Composable
fun SecondaryActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 54.dp
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Gray100,
            contentColor = Gray400
        )
    ) {
        Text(
            text = text,
            style = HaloType.body02SemiBold.copy(fontSize = 15.sp)
        )
    }
}
