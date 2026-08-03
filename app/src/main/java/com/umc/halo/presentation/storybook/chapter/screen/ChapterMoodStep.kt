package com.umc.halo.presentation.storybook.chapter.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.umc.halo.R
import com.umc.halo.domain.model.storybook.Chapter
import com.umc.halo.presentation.component.HaloTopBar
import com.umc.halo.presentation.storybook.chapter.ChapterMood
import com.umc.halo.presentation.storybook.chapter.component.ChapterBottomAction
import com.umc.halo.presentation.storybook.chapter.component.ChapterStepProgressBar
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.Gray50
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.White

private val MoodTitleColor = Color(0xFF0D0D0D)
private val MoodTagBackground = Color(0xFFFFFAF7)
private val MoodTagTextColor = Color(0xFFFF9742)

private data class MoodCardSpec(
    val mood: ChapterMood,
    val label: String,
    val imageResId: Int,
    val selectedBackgroundColor: Color
)

private val MoodCards = listOf(
    MoodCardSpec(
        mood = ChapterMood.THANKFUL,
        label = "감사했어요",
        imageResId = R.drawable.ic_mood_thankful,
        selectedBackgroundColor = Color(0xFFF5EDEF)
    ),
    MoodCardSpec(
        mood = ChapterMood.SAD,
        label = "슬펐어요",
        imageResId = R.drawable.ic_mood_sad,
        selectedBackgroundColor = Color(0xFFF1EDE6)
    ),
    MoodCardSpec(
        mood = ChapterMood.THOUGHTFUL,
        label = "생각이 많아요",
        imageResId = R.drawable.ic_mood_thoughtful,
        selectedBackgroundColor = Color(0xFFEBF3EC)
    ),
    MoodCardSpec(
        mood = ChapterMood.ANGRY,
        label = "화가 났어요",
        imageResId = R.drawable.ic_mood_angry,
        selectedBackgroundColor = Color(0xFFEBEBEB)
    ),
    MoodCardSpec(
        mood = ChapterMood.AWKWARD,
        label = "어색했어요",
        imageResId = R.drawable.ic_mood_awkward,
        selectedBackgroundColor = Color(0xFFE0EAF5)
    ),
    MoodCardSpec(
        mood = ChapterMood.HAPPY,
        label = "즐거웠어요",
        imageResId = R.drawable.ic_mood_happy,
        selectedBackgroundColor = Color(0xFFF6F0DD)
    )
)

@Composable
fun ChapterMoodStep(
    chapter: Chapter,
    selectedMood: ChapterMood?,
    isNextEnabled: Boolean,
    onMoodClick: (ChapterMood) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        HaloTopBar(
            title = chapter.storybookTitle,
            showLeftIcon = true,
            onClick = onBackClick
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 110.dp)
            ) {
                Spacer(modifier = Modifier.height(18.dp))

                ChapterStepProgressBar(
                    currentStepIndex = 2,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(25.dp))

                ChapterMoodHeader(
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(28.dp))

                MoodCardGrid(
                    selectedMood = selectedMood,
                    onMoodClick = onMoodClick,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            ChapterBottomAction(
                text = "다음",
                enabled = isNextEnabled,
                onClick = onNextClick,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun ChapterMoodHeader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Surface(
            color = MoodTagBackground,
            shape = RoundedCornerShape(100.dp)
        ) {
            Text(
                text = "03",
                style = HaloType.body03Medium,
                color = MoodTagTextColor,
                modifier = Modifier.padding(
                    horizontal = 10.dp,
                    vertical = 8.dp
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "지금까지 기록하며\n느낀 감정을 선택해볼까요?",
            style = HaloType.heading03SemiBold,
            color = MoodTitleColor
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "이 장면을 남기며 가장 가까웠던 마음을 선택해요.",
            style = HaloType.body03Regular,
            color = Gray400
        )
    }
}

@Composable
private fun MoodCardGrid(
    selectedMood: ChapterMood?,
    onMoodClick: (ChapterMood) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.width(234.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MoodCards.chunked(2).forEach { rowItems ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEach { spec ->
                    MoodCard(
                        spec = spec,
                        selected = selectedMood == spec.mood,
                        onClick = {
                            onMoodClick(spec.mood)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun MoodCard(
    spec: MoodCardSpec,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) {
        spec.selectedBackgroundColor
    } else {
        Gray50
    }

    Column(
        modifier = Modifier
            .size(110.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = spec.imageResId),
            contentDescription = spec.label,
            modifier = Modifier.size(56.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = spec.label,
            style = HaloType.body02Regular,
            color = Gray800,
            textAlign = TextAlign.Center
        )
    }
}
