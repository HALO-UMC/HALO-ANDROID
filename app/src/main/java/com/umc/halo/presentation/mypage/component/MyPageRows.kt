package com.umc.halo.presentation.mypage.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.White

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        style = HaloType.body01SemiBold.copy(fontSize = 17.sp),
        color = Gray800
    )
}

@Composable
fun ProfileCard() {
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
                    style = HaloType.heading03SemiBold.copy(fontSize = 19.sp),
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
fun MenuRow(
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
            style = HaloType.body02Medium.copy(fontSize = 15.sp),
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
    HorizontalDivider(color = Gray100)
}

@Composable
fun SettingSwitchRow(
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
                style = HaloType.body02SemiBold.copy(fontSize = 15.sp),
                color = Gray800
            )
            if (description != null) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = description,
                    style = HaloType.caption01Medium.copy(fontSize = 10.5.sp),
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
fun HaloSwitch(
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
fun InfoRow(
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
            style = HaloType.body02Medium.copy(fontSize = 15.sp),
            color = Gray800,
            modifier = Modifier.weight(0.9f)
        )
        Text(
            text = value,
            style = HaloType.body03Regular.copy(fontSize = 12.5.sp),
            color = Gray700,
            textAlign = TextAlign.End,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1.1f)
        )
    }
    HorizontalDivider(color = Gray100)
}

@Composable
fun TimeSettingCard(
    timeText: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(com.umc.halo.presentation.theme.Primary30)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = timeText,
            style = HaloType.body03Medium.copy(fontSize = 11.5.sp),
            color = com.umc.halo.presentation.theme.Primary600,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_home_right_arrow),
            contentDescription = null,
            tint = com.umc.halo.presentation.theme.Primary600,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
fun WarningLine(text: String) {
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
            style = HaloType.body03Regular.copy(fontSize = 12.5.sp),
            color = Gray600
        )
    }
}
