package com.umc.halo.presentation.calendar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.umc.halo.R
import com.umc.halo.domain.model.calendar.DateCompletedChapter
import com.umc.halo.domain.model.calendar.DateCompletedStorybook
import com.umc.halo.domain.model.calendar.DayRecord
import com.umc.halo.presentation.theme.Gray50
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary30
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.White

/**
 * 날짜 클릭 시 뜨는 기록 모달
 * - record.hasRecord == true  : (완성 스토리북) + (장 기록중 리스트)
 * - record.hasRecord == false : "아직 기록이 없어요!" + '시작하기' CTA
 */
@Composable
fun CalendarDayRecordModal(
    record: DayRecord,
    onEvent: (CalendarUiEvent) -> Unit
) {
    Dialog(
        onDismissRequest = { onEvent(CalendarUiEvent.OnDismissModal) },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(White)
                .heightIn(max = 640.dp)             // 내용이 길면 스크롤로
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // 헤더: 날짜 + 닫기
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = String.format("%02d월 %02d일 기록", record.month, record.day),
                    style = HaloType.body01SemiBold,
                    color = Gray800,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    painter = painterResource(R.drawable.ic_common_close),
                    contentDescription = "닫기",
                    tint = Gray800,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onEvent(CalendarUiEvent.OnDismissModal) }
                )
            }

            if (record.hasRecord) {
                // 완성 스토리북(있으면 먼저)
                record.completedStorybook?.let { completed ->
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("완성 스토리북", style = HaloType.body02SemiBold, color = Gray800)
                        CompletedCard(
                            item = completed,
                            onClick = {
                                onEvent(CalendarUiEvent.OnCompletedStorybookClicked(completed.storybookId))
                            }
                        )
                    }
                }
                // 장 기록중(그 날 완료한 장, 최신순)
                if (record.completedChapters.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("장 기록중", style = HaloType.body02SemiBold, color = Gray800)
                        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                            record.completedChapters.forEach { chapter ->
                                ChapterCard(
                                    item = chapter,
                                    onClick = {
                                        onEvent(
                                            CalendarUiEvent.OnChapterClicked(
                                                chapter.storybookId,
                                                chapter.chapter
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            } else {
                // 기록 없음
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("아직 기록이 없어요!", style = HaloType.heading02SemiBold, color = Gray800)
                    Text(
                        "쉬운 테마부터 바로 시작해보세요!",
                        style = HaloType.caption01Regular,
                        color = Gray500
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    ChapterBadge(
                        text = "시작하기",
                        onClick = { onEvent(CalendarUiEvent.OnStartRecordingClicked) }
                    )
                }
            }
        }
    }
}

/** 완성 스토리북 카드 (커버 + 제목 + "이 테마를 완성한 날이네요!" + >) */
@Composable
private fun CompletedCard(
    item: DateCompletedStorybook,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, shape)
            .clip(shape)
            .background(androidx.compose.ui.graphics.Color.White)
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 커버 이미지
        // TODO: [임시] 모든 카드가 같은 더미 커버를 씀 — 서버에서 스토리북별 커버가 내려오면 교체
        Box(
            modifier = Modifier
                .width(57.dp)
                .height(68.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Gray50)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_storybook_customsection_dummy),
                contentDescription = null,      // 장식용 이미지라 null
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(item.title, style = HaloType.body01SemiBold, color = Gray800)
            Text(
                "이 테마를 완성한 날이네요!",
                style = HaloType.caption01Regular,
                color = Gray500
            )
        }
        Icon(
            painter = painterResource(R.drawable.ic_home_right_arrow),
            contentDescription = null,
            tint = Gray800,
            modifier = Modifier.size(18.dp)
        )
    }
}

/** 장 기록중 카드 (제목 + "N장 기록을 완료했어요!" + >) → 클릭 시 스토리북 상세(완료) */
@Composable
private fun ChapterCard(
    item: DateCompletedChapter,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, shape)
            .clip(shape)
            .background(androidx.compose.ui.graphics.Color.White)
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(item.title, style = HaloType.body01SemiBold, color = Gray800)
            Text(
                "${item.chapter}장 기록을 완료했어요!",
                style = HaloType.body03Regular,
                color = Gray500
            )
        }
        Icon(
            painter = painterResource(R.drawable.ic_home_right_arrow),
            contentDescription = null,
            tint = Gray800,
            modifier = Modifier.size(18.dp)
        )
    }
}

/** "시작하기 >" 버튼 — 빈 모달의 '시작하기' CTA 에서 사용 */
@Composable
private fun ChapterBadge(
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(Primary30)
            .clickable { onClick() }
            .height(36.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(text, style = HaloType.body02Medium, color = Primary500)
        Icon(
            painter = painterResource(R.drawable.ic_home_right_arrow),
            contentDescription = null,
            tint = Primary500,
            modifier = Modifier.size(18.dp)
        )
    }
}
