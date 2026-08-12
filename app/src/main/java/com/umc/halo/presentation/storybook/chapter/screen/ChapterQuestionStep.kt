package com.umc.halo.presentation.storybook.chapter.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.umc.halo.domain.model.storybook.Chapter
import com.umc.halo.presentation.component.HaloTopBar
import com.umc.halo.presentation.storybook.chapter.component.ChapterAnswerTextField
import com.umc.halo.presentation.storybook.chapter.component.ChapterBottomAction
import com.umc.halo.presentation.storybook.chapter.component.ChapterStepProgressBar
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.White

private val QuestionTitleColor = Color(0xFF0D0D0D)
private val QuestionTagBackground = Color(0xFFFFFAF7)
private val QuestionTagTextColor = Color(0xFFFF9742)

@Composable
fun ChapterQuestionStep(
    chapter: Chapter,
    answers: List<String>,
    isNextEnabled: Boolean,
    onAnswerChanged: (questionIndex: Int, answer: String) -> Unit,
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
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 110.dp)
            ) {
                Spacer(modifier = Modifier.height(18.dp))

                ChapterStepProgressBar(
                    currentStepIndex = 0,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(25.dp))

                ChapterQuestionHeader(
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(36.dp))

                Column(
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    chapter.questions.forEachIndexed { index, question ->
                        ChapterQuestionItem(
                            questionNumber = index + 1,
                            question = question.question,
                            answer = answers.getOrElse(index) { "" },
                            onAnswerChanged = { answer ->
                                onAnswerChanged(index, answer)
                            }
                        )

                        if (index != chapter.questions.lastIndex) {
                            Spacer(modifier = Modifier.height(36.dp))
                        }
                    }
                }
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
private fun ChapterQuestionHeader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Surface(
            color = QuestionTagBackground,
            shape = RoundedCornerShape(100.dp)
        ) {
            Text(
                text = "01",
                style = HaloType.body03Medium,
                color = QuestionTagTextColor,
                modifier = Modifier.padding(
                    horizontal = 10.dp,
                    vertical = 8.dp
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "오늘의 한 페이지를 채워볼까요?",
            style = HaloType.heading03SemiBold,
            color = QuestionTitleColor
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "정답 없이, 떠오르는 장면과 마음을 편하게 남겨요.",
            style = HaloType.body03Regular,
            color = Gray400
        )
    }
}

@Composable
private fun ChapterQuestionItem(
    questionNumber: Int,
    question: String,
    answer: String,
    onAnswerChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "$questionNumber. $question",
            style = HaloType.body02Regular,
            color = Gray800
        )

        Spacer(modifier = Modifier.height(12.dp))

        ChapterAnswerTextField(
            value = answer,
            onValueChange = onAnswerChanged
        )
    }
}
