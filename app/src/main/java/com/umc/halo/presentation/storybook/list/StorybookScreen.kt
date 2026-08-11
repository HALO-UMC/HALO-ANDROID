package com.umc.halo.presentation.storybook.list

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.umc.halo.domain.model.storybook.CustomStorybook
import com.umc.halo.domain.model.storybook.InProgressStorybook
import com.umc.halo.domain.model.storybook.Storybook
import com.umc.halo.domain.model.storybook.StorybookProgress
import com.umc.halo.domain.model.storybook.StorybookTheme
import com.umc.halo.presentation.component.CustomStorybookCard
import com.umc.halo.presentation.storybook.chapter.component.HaloLoadFailed
import com.umc.halo.presentation.storybook.chapter.component.HaloLoading
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.Gray700
import com.umc.halo.presentation.theme.HaloTheme
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary100
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.White

// 섹션 제목 색
private val SectionTitleColor = Color(0xFF3C3A35)

// 좌우 공통 가로 패딩
private val HorizontalPadding = 24.dp

// 진행중/완료 2열 그리드
private val GridColumnSpacing = 16.dp
private val GridRowSpacing = 36.dp

// 전체 탭 섹션 간격
private val ThemeCardSpacing = 16.dp             // 테마 가로 스크롤 카드 사이
private val CustomSectionBottomSpacing = 36.dp   // 맞춤 섹션 ↔ 첫 테마 섹션
private val ThemeSectionSpacing = 48.dp          // 테마 섹션 ↔ 테마 섹션

/**
 * 스토리북 목록 화면 진입점
 *
 * 화면 이동은 [onNavigateToStorybookDetail]·[onNavigateToThemeBox] 콜백으로 위임
 *
 * @param onNavigateToThemeBox 완료한 스토리북 → 테마함. 그 스토리북의 테마를 펼쳐서 열어야 하므로 id 를 넘김
 */
@Composable
fun StorybookScreen(
    vm: StorybookViewModel = hiltViewModel(),
    onNavigateToStorybookDetail: (Long) -> Unit = {},
    onNavigateToThemeBox: (Long) -> Unit = {}
) {
    val state by vm.uiState.collectAsState()
    val context = LocalContext.current

    // 화면이 보일 때마다 조회
    LaunchedEffect(Unit) {
        vm.onEvent(StorybookUiEvent.OnScreenShown)
    }

    // 조회 실패 안내
    // TODO: 표시 방식은 디자인 확정 후 교체 (지금은 토스트)
    LaunchedEffect(state.errorMessage) {
        val message = state.errorMessage ?: return@LaunchedEffect
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        vm.onEvent(StorybookUiEvent.ErrorShown)
    }

    StorybookContent(
        state = state,
        onEvent = { event ->
            when (event) {
                // 카드 클릭 = 화면 이동 → 화면 경계에서 처리
                is StorybookUiEvent.OnCustomStorybookClicked ->
                    onNavigateToStorybookDetail(event.storybookId)

                is StorybookUiEvent.OnStorybookClicked ->
                    onNavigateToStorybookDetail(event.storybookId)

                is StorybookUiEvent.OnContinueStorybookClicked ->
                    onNavigateToStorybookDetail(event.storybookId)

                // 완료한 책은 테마함의 해당 스토리북으로 (완료 탭 카드 + 전체 탭 '완료' 배지 카드 공통)
                is StorybookUiEvent.OnDoneStorybookClicked ->
                    onNavigateToThemeBox(event.storybookId)

                // 그 외(탭 전환·조회)는 VM 이 상태로 처리
                else -> vm.onEvent(event)
            }
        }
    )
}

@Composable
private fun StorybookContent(
    state: StorybookUiState,
    onEvent: (StorybookUiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        Spacer(Modifier.height(26.dp))

        StorybookSegmentedTabs(
            selectedTab = state.selectedTab,
            onTabSelected = { onEvent(StorybookUiEvent.OnTabSelected(it)) }
        )

        Spacer(Modifier.height(24.dp))

        when {
            state.isLoading -> HaloLoading()

            state.hasLoadFailed -> HaloLoadFailed(
                text = "스토리북",
                onRetry = { onEvent(StorybookUiEvent.OnRetryClicked) }
            )

            else -> when (state.selectedTab) {
                StorybookTab.ALL -> StorybookAllList(state = state, onEvent = onEvent)

                StorybookTab.IN_PROGRESS -> StorybookGridSection(
                    title = "진행중인 스토리북",
                    items = state.inProgressStorybooks,
                    key = { it.id }
                ) { book, cardModifier ->
                    StorybookCard(
                        title = book.title,
                        subtitle = book.subtitle,
                        modifier = cardModifier,
                        coverUrl = book.imageUrl,
                        badge = StorybookBadge.InProgress(book.currentChapter),
                        isWaiting = book.isWaiting,
                        onClick = {
                            onEvent(StorybookUiEvent.OnContinueStorybookClicked(book.id))
                        }
                    )
                }

                StorybookTab.DONE -> StorybookGridSection(
                    title = "완료한 스토리북",
                    items = state.doneStorybooks,
                    key = { it.id }
                ) { book, cardModifier ->
                    StorybookCard(
                        title = book.title,
                        subtitle = book.subtitle,
                        modifier = cardModifier,
                        coverUrl = book.imageUrl,
                        badge = StorybookBadge.Done,
                        onClick = { onEvent(StorybookUiEvent.OnDoneStorybookClicked(book.id)) }
                    )
                }
            }
        }
    }
}

/**
 * 전체/진행중/완료 탭
 * 탭 했을 경우와 탭 하지 않았을 경우로 구분
 */
@Composable
private fun StorybookSegmentedTabs(
    selectedTab: StorybookTab,
    onTabSelected: (StorybookTab) -> Unit
) {
    Row(
        modifier = Modifier.padding(horizontal = HorizontalPadding),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StorybookTab.entries.forEach { tab ->
            SegmentedTabButton(
                text = tab.label,
                selected = tab == selectedTab,
                onClick = { onTabSelected(tab) }
            )
        }
    }
}

@Composable
private fun SegmentedTabButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(100.dp)
    Box(
        modifier = Modifier
            .alpha(if (selected) 1f else 0.4f) // 미선택 탭 전체를 흐리게
            .width(64.dp)
            .height(36.dp)
            .clip(shape)
            .background(if (selected) Primary100 else White)
            .border(
                width = 1.dp,
                color = if (selected) Color.Transparent else Gray100,
                shape = shape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = if (selected) HaloType.body02Medium else HaloType.body02Regular,
            color = if (selected) Primary500 else Gray700
        )
    }
}

/**
 * '전체' 탭 : 맞춤 스토리북 섹션 + 상황별 테마 섹션들
 */
@Composable
private fun StorybookAllList(
    state: StorybookUiState,
    onEvent: (StorybookUiEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 30.dp)
    ) {
        // 맞춤 스토리북
        if (state.customStorybooks.isNotEmpty()) {
            item {
                Column {
                    CustomStorybookSection(
                        userName = state.userName,
                        items = state.customStorybooks,
                        onClick = { onEvent(StorybookUiEvent.OnCustomStorybookClicked(it)) }
                    )
                    Spacer(Modifier.height(CustomSectionBottomSpacing))
                }
            }
        }

        // 상황별 테마 섹션들 (섹션 사이 간격이 맞춤 섹션과 달라 앞쪽에 간격을 붙임)
        itemsIndexed(
            items = state.themes,
            // 서버가 섹션 id 를 주지 않아 상황 태그를 식별자로 사용
            key = { _, theme -> theme.title }
        ) { index, theme ->
            Column {
                if (index > 0) Spacer(Modifier.height(ThemeSectionSpacing))
                StorybookThemeSection(
                    theme = theme,
                    onCardClick = { book -> onEvent(book.toClickEvent()) }
                )
            }
        }
    }
}

/**
 * 맞춤 스토리북 섹션
 */
@Composable
private fun CustomStorybookSection(
    userName: String,
    items: List<CustomStorybook>,
    onClick: (Long) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = HorizontalPadding)) {
        Text(
            // 이름 조회에 실패했을 때 "님 맞춤 스토리북" 으로 보이지 않도록
            text = if (userName.isBlank()) "맞춤 스토리북" else "${userName}님 맞춤 스토리북",
            style = HaloType.body01SemiBold,
            color = SectionTitleColor
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = "부모님과의 관계를 개선하고 싶은 당신에게...",
            style = HaloType.body03Regular,
            color = Gray400
        )

        Spacer(Modifier.height(18.dp))

        // 맞춤 카드를 세로로 배치
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items.forEach { item ->
                CustomStorybookCard(
                    item = item,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onClick(item.id) }
                )
            }
        }
    }
}

/**
 * 상황별 테마 섹션
 */
@Composable
private fun StorybookThemeSection(
    theme: StorybookTheme,
    onCardClick: (Storybook) -> Unit
) {
    Column {
        Text(
            text = theme.title,
            style = HaloType.body01SemiBold,
            color = SectionTitleColor,
            modifier = Modifier.padding(horizontal = HorizontalPadding)
        )

        Spacer(Modifier.height(18.dp))

        // 가로 스크롤이라 weight 를 못 쓰므로 남는 폭을 2등분해 카드 폭을 직접 계산한다.
        // (2열 그리드와 같은 폭이 나와야 섹션끼리 좌우가 맞음)
        BoxWithConstraints {
            val cardWidth = (maxWidth - HorizontalPadding * 2 - ThemeCardSpacing) / 2

            LazyRow(
                contentPadding = PaddingValues(horizontal = HorizontalPadding),
                horizontalArrangement = Arrangement.spacedBy(ThemeCardSpacing)
            ) {
                itemsIndexed(
                    items = theme.storybooks,
                    key = { _, book -> book.id }
                ) { _, book ->
                    StorybookCard(
                        title = book.title,
                        subtitle = book.subtitle,
                        modifier = Modifier.width(cardWidth),
                        coverUrl = book.imageUrl,
                        badge = book.progress?.toStorybookBadge(),  // 책갈피
                        onClick = { onCardClick(book) }
                    )
                }
            }
        }
    }
}

/**
 * 진행중 / 완료 탭 공통 골격
 *
 * 카드 폭은 남는 폭을 2등분해서 정함
 */
@Composable
private fun <T> StorybookGridSection(
    title: String,
    items: List<T>,
    key: (T) -> Any,
    card: @Composable (T, Modifier) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 30.dp)
    ) {
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = title,
                    style = HaloType.body01SemiBold,
                    color = SectionTitleColor,
                    modifier = Modifier.padding(start = HorizontalPadding)
                )
                Spacer(Modifier.height(18.dp)) // 제목 → 그리드 간격
            }
        }
        itemsIndexed(
            items = items.chunked(2),
            key = { _, row -> key(row.first()) }
        ) { index, rowItems ->
            Column(modifier = Modifier.fillMaxWidth()) {
                if (index > 0) Spacer(Modifier.height(GridRowSpacing)) // 행 간격
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = HorizontalPadding),
                    horizontalArrangement = Arrangement.spacedBy(GridColumnSpacing)
                ) {
                    rowItems.forEach { item -> card(item, Modifier.weight(1f)) }
                    // 개수가 홀수라 마지막 줄에 카드가 하나면 빈 칸을 채워 카드 폭을 유지
                    if (rowItems.size < 2) Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

/** 도메인 진행상태 → 카드 배지(책갈피)
 * 도메인 StorybookProgress → presentation StorybookBadge
 **/
private fun StorybookProgress.toStorybookBadge(): StorybookBadge = when (this) {
    is StorybookProgress.InProgress -> StorybookBadge.InProgress(chapter)
    StorybookProgress.Done -> StorybookBadge.Done
}

/**
 * 전체 탭 카드의 클릭 목적지는 카드에 붙은 배지가 결정
 *
 * - 배지 없음(아직 시작 전) → 스토리북 목차
 * - "N장 진행중"           → 진행중 탭 카드와 동일하게 스토리북 목차
 * - "완료"                 → 완료 탭 카드와 동일하게 테마함
 */
private fun Storybook.toClickEvent(): StorybookUiEvent = when (progress) {
    null -> StorybookUiEvent.OnStorybookClicked(id)
    is StorybookProgress.InProgress -> StorybookUiEvent.OnContinueStorybookClicked(id)
    StorybookProgress.Done -> StorybookUiEvent.OnDoneStorybookClicked(id)
}

@Preview(showBackground = true, showSystemUi = true, name = "스토리북 - 전체")
@Composable
private fun StorybookAllTabPreview() {
    HaloTheme {
        StorybookContent(state = previewState(StorybookTab.ALL), onEvent = {})
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "스토리북 - 진행중")
@Composable
private fun StorybookInProgressTabPreview() {
    HaloTheme {
        StorybookContent(state = previewState(StorybookTab.IN_PROGRESS), onEvent = {})
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "스토리북 - 완료")
@Composable
private fun StorybookDoneTabPreview() {
    HaloTheme {
        StorybookContent(state = previewState(StorybookTab.DONE), onEvent = {})
    }
}

private fun previewState(tab: StorybookTab) = StorybookUiState(
    userName = "주연",
    selectedTab = tab,
    customStorybooks = listOf(
        CustomStorybook(1, "어색하지 않게 이야기하고 싶은 당신에게", "오래전 당신", "부모가 아닌 한 사람의 시간", ""),
        CustomStorybook(2, "부모님을 더 알고 싶은 당신에게", "당신 사용설명서", "가족과의 만남", "")
    ),
    themes = listOf(
        StorybookTheme(
            title = "어색하지 않게 이야기하고 싶어요",
            storybooks = listOf(
                Storybook(1, "오래전 당신", "부모가 아닌 한 사람의 시간", progress = StorybookProgress.InProgress(4)),
                Storybook(8, "한 장의 가족사진", "가족과의 만남", progress = StorybookProgress.Done)
            )
        ),
        StorybookTheme(
            title = "부모님을 더 알고 싶어요",
            storybooks = listOf(
                Storybook(3, "가족의 온도", "가족과의 만남"),
                Storybook(2, "당신 사용설명서", "가족과의 만남")
            )
        )
    ),
    inProgressStorybooks = listOf(
        InProgressStorybook(1, "오래전 당신", "가족과의 만남", currentChapter = 4, isWaiting = false),
        InProgressStorybook(3, "가족의 온도", "가족과의 만남", currentChapter = 2, isWaiting = true)
    ),
    doneStorybooks = listOf(
        Storybook(8, "한 장의 가족사진", "가족과의 만남", progress = StorybookProgress.Done)
    )
)
