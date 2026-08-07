package com.umc.halo.presentation.storybook.chapter

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.umc.halo.R
import com.umc.halo.domain.model.storybook.CompletedChapter
import com.umc.halo.presentation.storybook.chapter.component.ChapterImagePlaceholder
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray300
import com.umc.halo.presentation.theme.Gray50
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary400
import com.umc.halo.presentation.theme.Primary50
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.White

private val ResultTitleColor = Color(0xFF0D0D0D)
private val ResultInputBackground = Color(0xFFFAFAFA)

@Composable
fun ChapterResultRoute(
    memberChapterId: Long,
    onNavigateBack: () -> Unit,
    viewModel: ChapterResultViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(memberChapterId) {
        viewModel.loadCompletedChapter(memberChapterId)
    }

    LaunchedEffect(state.errorMessage) {
        val message = state.errorMessage ?: return@LaunchedEffect
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        viewModel.clearError()
    }

    BackHandler {
        onNavigateBack()
    }

    ChapterResultScreen(
        state = state,
        onBackClick = onNavigateBack
    )
}

@Composable
private fun ChapterResultScreen(
    state: ChapterResultUiState,
    onBackClick: () -> Unit
) {
    val completedChapter = state.completedChapter

    if (state.isLoading || completedChapter == null) {
        ChapterResultLoadingScreen()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .verticalScroll(rememberScrollState())
    ) {
        ChapterResultHero(
            completedChapter = completedChapter,
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 24.dp,
                    top = 32.dp,
                    end = 24.dp,
                    bottom = 48.dp
                )
        ) {
            Text(
                text = "완성된 스토리북 돌아보기",
                style = HaloType.heading03SemiBold,
                color = ResultTitleColor
            )

            Spacer(modifier = Modifier.height(28.dp))

            ResultQuestionSection(
                completedChapter = completedChapter
            )

            Spacer(modifier = Modifier.height(30.dp))

            ResultSceneSection(
                imageUrl = state.sceneImageUrl
            )

            Spacer(modifier = Modifier.height(30.dp))

            ResultMoodSection(
                mood = state.selectedMood
            )
        }
    }
}

@Composable
private fun ChapterResultLoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ChapterResultHero(
    completedChapter: CompletedChapter,
    onBackClick: () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        val heroHeight = maxWidth * 286f / 360f
        val heroShape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomEnd = 20.dp,
            bottomStart = 20.dp
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(heroHeight)
                .clip(heroShape)
        ) {
            ChapterImagePlaceholder(
                imageUrl = completedChapter.chapterImageUrl,
                showLabel = false,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f to Color.Transparent,
                                0.42f to Color.Transparent,
                                1.0f to Color.Black.copy(alpha = 0.66f)
                            )
                        )
                    )
            )

            ResultHeroTopBar(
                title = completedChapter.storybookTitle,
                onBackClick = onBackClick,
                modifier = Modifier.align(Alignment.TopCenter)
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        bottom = 32.dp
                    )
            ) {
                ResultChapterTag(
                    chapterNumber = completedChapter.chapterOrder
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = completedChapter.chapterTitle,
                    style = HaloType.heading02SemiBold,
                    color = White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = completedChapter.description,
                    style = HaloType.body02Regular,
                    color = White
                )
            }
        }
    }
}

@Composable
private fun ResultHeroTopBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .height(56.dp)
    ) {
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(40.dp),
            onClick = onBackClick
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_common_chevron_left),
                contentDescription = "뒤로가기",
                tint = White,
                modifier = Modifier.size(8.dp, 12.dp)
            )
        }

        Text(
            text = title,
            modifier = Modifier.align(Alignment.Center),
            style = HaloType.body01SemiBold,
            color = White
        )
    }
}

@Composable
private fun ResultQuestionSection(
    completedChapter: CompletedChapter
) {
    ResultSectionTitle(
        number = "01",
        title = "기록하기"
    )

    Spacer(modifier = Modifier.height(18.dp))

    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        completedChapter.answers.forEachIndexed { index, answer ->
            ResultQuestionAnswer(
                questionNumber = index + 1,
                question = answer.question,
                answer = answer.answer
            )
        }
    }
}

@Composable
private fun ResultQuestionAnswer(
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
            color = ResultTitleColor
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(ResultInputBackground)
                .border(
                    width = 1.dp,
                    color = Gray100,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = answer.ifBlank { "답변" },
                style = HaloType.body02Medium,
                color = if (answer.isBlank()) Gray300 else Gray800
            )
        }
    }
}

@Composable
private fun ResultSceneSection(
    imageUrl: String?
) {
    ResultSectionTitle(
        number = "02",
        title = "장면 확인하기"
    )

    Spacer(modifier = Modifier.height(12.dp))

    if (imageUrl.isNullOrBlank()) {
        ChapterImagePlaceholder(
            imageUrl = null,
            showLabel = true,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(312f / 294f)
                .clip(RoundedCornerShape(10.dp))
        )
    } else {
        AsyncImage(
            model = imageUrl,
            contentDescription = "선택한 장면",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(312f / 294f)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun ResultMoodSection(
    mood: ChapterMood
) {
    ResultSectionTitle(
        number = "03",
        title = "감정 기록"
    )

    Spacer(modifier = Modifier.height(12.dp))

    Column(
        modifier = Modifier
            .size(120.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Gray50),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = mood.toResultMoodImageResId()),
            contentDescription = mood.toResultMoodLabel(),
            modifier = Modifier.size(56.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = mood.toResultMoodLabel(),
            style = HaloType.body02Regular,
            color = Gray800,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ResultSectionTitle(
    number: String,
    title: String
) {
    Row(
        modifier = Modifier.height(30.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            color = Primary50,
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
            color = ResultTitleColor
        )
    }
}

@Composable
private fun ResultChapterTag(
    chapterNumber: Int
) {
    Surface(
        shape = RoundedCornerShape(100.dp),
        color = Primary50
    ) {
        Text(
            text = "${chapterNumber.toString().padStart(2, '0')}장",
            style = HaloType.body03Medium,
            color = Primary500,
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 8.dp
            )
        )
    }
}

private fun ChapterMood.toResultMoodLabel(): String =
    when (this) {
        ChapterMood.THANKFUL -> "감사했어요"
        ChapterMood.SAD -> "슬펐어요"
        ChapterMood.THOUGHTFUL -> "생각이 많아요"
        ChapterMood.ANGRY -> "화가 났어요"
        ChapterMood.AWKWARD -> "어색했어요"
        ChapterMood.HAPPY -> "즐거웠어요"
    }

private fun ChapterMood.toResultMoodImageResId(): Int =
    when (this) {
        ChapterMood.THANKFUL -> R.drawable.ic_mood_thankful
        ChapterMood.SAD -> R.drawable.ic_mood_sad
        ChapterMood.THOUGHTFUL -> R.drawable.ic_mood_thoughtful
        ChapterMood.ANGRY -> R.drawable.ic_mood_angry
        ChapterMood.AWKWARD -> R.drawable.ic_mood_awkward
        ChapterMood.HAPPY -> R.drawable.ic_mood_happy
    }
