package com.umc.halo.presentation.storybook.chapter.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.umc.halo.R
import com.umc.halo.domain.model.storybook.Chapter
import com.umc.halo.domain.model.storybook.ChapterSceneCard
import com.umc.halo.presentation.component.HaloTopBar
import com.umc.halo.presentation.storybook.chapter.ChapterMood
import com.umc.halo.presentation.storybook.chapter.ChapterSceneRecordMethod
import com.umc.halo.presentation.storybook.chapter.component.ChapterBottomAction
import com.umc.halo.presentation.storybook.chapter.component.ChapterSceneCardImage
import com.umc.halo.presentation.storybook.chapter.component.rememberSelectedImageBitmap
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.Gray50
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary400
import com.umc.halo.presentation.theme.White

private val ReviewTitleColor = Color(0xFF0D0D0D)
private val ReviewTagBackground = Color(0xFFFFFAF7)
private val ReviewInputBackground = Color(0xFFFAFAFA)

private data class ReviewMoodSpec(
    val label: String,
    val imageResId: Int
)

@Composable
fun ChapterReviewStep(
    chapter: Chapter,
    answers: List<String>,
    selectedMethod: ChapterSceneRecordMethod?,
    selectedImageUri: String?,
    selectedSceneCard: ChapterSceneCard?,
    selectedMood: ChapterMood?,
    showBottomAction: Boolean = true,
    onBackClick: () -> Unit,
    onCompleteClick: () -> Unit = {}
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
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        bottom = if (showBottomAction) 122.dp else 32.dp
                    )
            ) {
                Text(
                    text = "오늘의 기록 확인하기",
                    style = HaloType.heading03SemiBold,
                    color = ReviewTitleColor
                )

                Spacer(modifier = Modifier.height(26.dp))

                ReviewQuestionSection(
                    chapter = chapter,
                    answers = answers
                )

                Spacer(modifier = Modifier.height(30.dp))

                ReviewSceneSection(
                    selectedMethod = selectedMethod,
                    selectedImageUri = selectedImageUri,
                    selectedSceneCard = selectedSceneCard
                )

                Spacer(modifier = Modifier.height(30.dp))

                ReviewMoodSection(
                    selectedMood = selectedMood
                )
            }

            if (showBottomAction) {
                ChapterBottomAction(
                    text = "완성하기",
                    enabled = true,
                    onClick = onCompleteClick,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}

@Composable
private fun ReviewQuestionSection(
    chapter: Chapter,
    answers: List<String>
) {
    ReviewSectionTitle(
        number = "01",
        title = "기록하기"
    )

    Spacer(modifier = Modifier.height(18.dp))

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        chapter.questions.forEachIndexed { index, question ->
            ReviewQuestionAnswer(
                questionNumber = index + 1,
                question = question.question,
                answer = answers.getOrElse(index) { "" }
            )
        }
    }
}

@Composable
private fun ReviewQuestionAnswer(
    questionNumber: Int,
    question: String,
    answer: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "$questionNumber. $question",
            style = HaloType.body02Regular,
            color = ReviewTitleColor
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(ReviewInputBackground)
                .border(
                    width = 1.dp,
                    color = Gray50,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = answer.ifBlank { "기록완료" },
                style = HaloType.body02Medium,
                color = Gray800
            )
        }
    }
}

@Composable
private fun ReviewSceneSection(
    selectedMethod: ChapterSceneRecordMethod?,
    selectedImageUri: String?,
    selectedSceneCard: ChapterSceneCard?
) {
    ReviewSectionTitle(
        number = "02",
        title = "장면 확인하기"
    )

    Spacer(modifier = Modifier.height(12.dp))

    ReviewScenePreview(
        selectedMethod = selectedMethod,
        selectedImageUri = selectedImageUri,
        selectedSceneCard = selectedSceneCard
    )
}

@Composable
private fun ReviewScenePreview(
    selectedMethod: ChapterSceneRecordMethod?,
    selectedImageUri: String?,
    selectedSceneCard: ChapterSceneCard?
) {
    when (selectedMethod) {
        ChapterSceneRecordMethod.PHOTO -> {
            ReviewPhotoImage(
                selectedImageUri = selectedImageUri
            )
        }

        ChapterSceneRecordMethod.SCENE_CARD -> {
            if (selectedSceneCard != null) {
                ChapterSceneCardImage(
                    card = selectedSceneCard,
                    cornerRadius = 10.dp,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            } else {
                ReviewEmptyPreview()
            }
        }

        null -> {
            ReviewEmptyPreview()
        }
    }
}

@Composable
private fun ReviewPhotoImage(
    selectedImageUri: String?
) {
    if (selectedImageUri?.startsWith("http") == true) {
        AsyncImage(
            model = selectedImageUri,
            contentDescription = "?좏깮???λ㈃ ?대?吏",
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )
        return
    }

    val imageBitmap = rememberSelectedImageBitmap(selectedImageUri)

    if (imageBitmap != null) {
        Image(
            bitmap = imageBitmap,
            contentDescription = "선택한 장면 이미지",
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )
    } else {
        ReviewEmptyPreview()
    }
}

@Composable
private fun ReviewEmptyPreview() {
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Gray50),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "장면 없음",
            style = HaloType.caption01Regular,
            color = Gray400
        )
    }
}

@Composable
private fun ReviewMoodSection(
    selectedMood: ChapterMood?
) {
    ReviewSectionTitle(
        number = "03",
        title = "감정 기록"
    )

    Spacer(modifier = Modifier.height(12.dp))

    val moodSpec = selectedMood?.toReviewMoodSpec()

    if (moodSpec != null) {
        ReviewMoodCard(
            spec = moodSpec
        )
    }
}

@Composable
private fun ReviewMoodCard(
    spec: ReviewMoodSpec
) {
    Column(
        modifier = Modifier
            .size(120.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Gray50),
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

@Composable
private fun ReviewSectionTitle(
    number: String,
    title: String
) {
    Row(
        modifier = Modifier.height(30.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            color = ReviewTagBackground,
            shape = RoundedCornerShape(100.dp)
        ) {
            Text(
                text = number,
                style = HaloType.body03Medium,
                color = Primary400,
                modifier = Modifier.padding(
                    horizontal = 10.dp,
                    vertical = 8.dp
                )
            )
        }

        Text(
            text = title,
            style = HaloType.body01Medium,
            color = ReviewTitleColor
        )
    }
}

private fun ChapterMood.toReviewMoodSpec(): ReviewMoodSpec {
    return when (this) {
        ChapterMood.THANKFUL -> ReviewMoodSpec(
            label = "감사했어요",
            imageResId = R.drawable.ic_mood_thankful
        )

        ChapterMood.SAD -> ReviewMoodSpec(
            label = "슬펐어요",
            imageResId = R.drawable.ic_mood_sad
        )

        ChapterMood.THOUGHTFUL -> ReviewMoodSpec(
            label = "생각이 많아요",
            imageResId = R.drawable.ic_mood_thoughtful
        )

        ChapterMood.ANGRY -> ReviewMoodSpec(
            label = "화가 났어요",
            imageResId = R.drawable.ic_mood_angry
        )

        ChapterMood.AWKWARD -> ReviewMoodSpec(
            label = "어색했어요",
            imageResId = R.drawable.ic_mood_awkward
        )

        ChapterMood.HAPPY -> ReviewMoodSpec(
            label = "즐거웠어요",
            imageResId = R.drawable.ic_mood_happy
        )
    }
}
