package com.umc.halo.presentation.mypage.component

import androidx.compose.foundation.Image
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.umc.halo.R
import com.umc.halo.presentation.theme.Gray100
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
        style = HaloType.body01SemiBold,
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
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = White,
                modifier = Modifier.size(72.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_orange_character),
                    contentDescription = null,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column {
                Text(
                    text = "주현AB",
                    style = HaloType.heading02SemiBold,
                    color = Gray800
                )
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
            style = HaloType.body02SemiBold,
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
    description: String? = null,
    enabled: Boolean = true,
    titleStyle: TextStyle = HaloType.body02SemiBold
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = titleStyle,
                color = if (enabled) Gray800 else Gray400
            )
            if (description != null) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = description,
                    style = HaloType.body03Regular,
                    color = if (enabled) Gray500 else Gray300
                )
            }
        }

        HaloSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
    }
}

@Composable
fun HaloSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 21.dp else 3.dp,
        label = "haloSwitchThumb"
    )

    Box(
        modifier = modifier
            .width(42.dp)
            .height(24.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(
                when {
                    !enabled -> Gray100
                    checked -> Gray600
                    else -> Gray100
                }
            )
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Switch,
                onValueChange = onCheckedChange
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(18.dp)
                .clip(CircleShape)
                .background(if (checked && enabled) White else Gray300)
        )
    }
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
            style = HaloType.body02SemiBold,
            color = Gray800,
            modifier = Modifier.weight(0.9f)
        )
        Text(
            text = value,
            style = if (value.length > 12) {
                HaloType.body03Regular
            } else {
                HaloType.body02Regular
            },
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
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(if (enabled) com.umc.halo.presentation.theme.Primary30 else Gray30)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = timeText,
            style = HaloType.body02Regular,
            color = if (enabled) com.umc.halo.presentation.theme.Primary600 else Gray400,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_home_right_arrow),
            contentDescription = null,
            tint = if (enabled) com.umc.halo.presentation.theme.Primary600 else Gray300,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun WarningLine(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = HaloType.body01Regular,
            color = Gray500
        )
    }
}
