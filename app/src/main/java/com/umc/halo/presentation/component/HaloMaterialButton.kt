package com.umc.halo.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray200
import com.umc.halo.presentation.theme.Gray30
import com.umc.halo.presentation.theme.Gray300
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.White

@Composable
fun HaloMaterialButton(
    buttonState: ButtonState,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val (containerColor, contentColor, borderColor) = when (buttonState) {
        ButtonState.ABLE -> Triple(
            Primary500,
            White,
            Color.Transparent
        )

        ButtonState.ENABLE -> Triple(
            Gray100,
            Gray400,
            Color.Transparent
        )

        ButtonState.DISABLED -> Triple(
            Gray200,
            Gray100,
            Color.Transparent
        )

        ButtonState.LINE -> Triple(
            Gray30,
            Gray300,
            Gray300
        )
    }

    Button(
        onClick = onClick,
        enabled = buttonState != ButtonState.DISABLED,
        modifier = modifier.border(
            width = if (buttonState == ButtonState.LINE) 1.dp else 0.dp,
            color = borderColor,
            shape = RoundedCornerShape(30.dp)
        ),
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = Gray200,
            disabledContentColor = Gray100
        )
    ) {
        Text(
            text = text,
            style = HaloType.body02Regular
        )
    }
}

enum class ButtonState{
    ABLE,
    ENABLE,
    DISABLED,
    LINE
}