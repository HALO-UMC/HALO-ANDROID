package com.umc.halo.presentation.storybook.chapter.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray300
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary500

private val AnswerFieldBackground = Color(0xFFFAFAFA)

@Composable
fun ChapterAnswerTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "기록해보세요."
) {
    val shape = RoundedCornerShape(16.dp)

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = HaloType.body02Regular.copy(
            color = Gray800
        ),
        cursorBrush = SolidColor(Primary500),
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = AnswerFieldBackground,
                        shape = shape
                    )
                    .border(
                        width = 1.dp,
                        color = Gray100,
                        shape = shape
                    )
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = HaloType.body02Regular,
                        color = Gray300
                    )
                }

                innerTextField()
            }
        }
    )
}