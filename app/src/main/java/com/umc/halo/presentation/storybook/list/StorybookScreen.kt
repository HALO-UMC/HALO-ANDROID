package com.umc.halo.presentation.storybook.list

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.umc.halo.R
import com.umc.halo.domain.model.storybook.CustomStorybook
import com.umc.halo.domain.model.storybook.StorybookProgress
import com.umc.halo.domain.model.storybook.StorybookTheme
import com.umc.halo.presentation.component.CustomStorybookCard
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
 */
@Composable
fun StorybookScreen(
    vm: StorybookViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    StorybookContent(
        state = state,
        onEvent = vm::onEvent
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

        when (state.selectedTab) {
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
                    badge = StorybookBadge.InProgress(book.currentChapter),
                    isWaiting = book.isWaiting,
                    onClick = {
                        onEvent(
                            StorybookUiEvent.OnContinueStorybookClicked(book.id, book.currentChapter)
                        )
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
                    badge = StorybookBadge.Done,
                    onClick = { onEvent(StorybookUiEvent.OnDoneStorybookClicked(book.id)) }
                )
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
            key = { _, theme -> theme.id }
        ) { index, theme ->
            Column {
                if (index > 0) Spacer(Modifier.height(ThemeSectionSpacing))
                StorybookThemeSection(
                    theme = theme,
                    isFirstSection = index == 0,
                    onClick = { onEvent(StorybookUiEvent.OnStorybookClicked(it)) }
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
    onClick: (Int) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = HorizontalPadding)) {
        Text(
            text = "${userName}님 맞춤 스토리북",
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
            items.forEachIndexed { index, item ->
                CustomStorybookCard(
                    item = item,
                    modifier = Modifier.fillMaxWidth(),
                    // [임시] 커버 더미 — 실제 커버 연동 시 제거
                    coverRes = if (index == 0) R.drawable.ic_storybook_customsection_dummy else null,
                    onClick = { onClick(item.id) }
                )
            }
        }
    }
}

/**
 * 상황별 테마 섹션
 *
 * @param isFirstSection [임시] 커버 더미를 첫 섹션 첫 카드에만 넣기 위한 표시
 */
@Composable
private fun StorybookThemeSection(
    theme: StorybookTheme,
    isFirstSection: Boolean,
    onClick: (Int) -> Unit
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
                ) { index, book ->
                    StorybookCard(
                        title = book.title,
                        subtitle = book.subtitle,
                        modifier = Modifier.width(cardWidth),
                        // [임시] 커버 더미 — 실제 커버 연동 시 제거
                        coverRes = if (isFirstSection && index == 0) {
                            R.drawable.ic_storybook_themesection_dummy
                        } else {
                            null
                        },
                        badge = book.progress?.toStorybookBadge(),  // 책갈피
                        onClick = { onClick(book.id) }
                    )
                }
            }
        }
    }
}

/**
 * 진행중 / 완료 탭 공통 골격
 *
 * 카드 폭은 고정값이 아니라 남는 폭을 2등분해서 정한다(호출부에 넘기는 modifier).
 * 그래야 좌우 여백이 [HorizontalPadding] 으로 정확히 대칭이 되고 기기 폭이 달라져도 대응된다.
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun StorybookScreenPreview() {
    HaloTheme {
        StorybookScreen()
    }
}
