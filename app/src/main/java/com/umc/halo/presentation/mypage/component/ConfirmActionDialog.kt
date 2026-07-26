package com.umc.halo.presentation.mypage.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.halo.R
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.Gray600
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.White

@Composable
fun ConfirmActionDialog(
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
                            style = HaloType.body02SemiBold.copy(fontSize = 15.sp),
                            color = Gray800
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = description,
                            style = HaloType.caption01Medium.copy(fontSize = 10.5.sp),
                            color = Gray600
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_common_close),
                            contentDescription = "닫기",
                            tint = Gray400
                        )
                    }
                }

                Spacer(Modifier.height(26.dp))

                PrimaryActionButton(
                    text = buttonText,
                    onClick = onConfirm
                )
            }
        }
    )
}
