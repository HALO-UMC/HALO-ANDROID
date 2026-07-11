package com.umc.halo.presentation.storybook.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.umc.halo.R
import com.umc.halo.domain.model.storybook.CustomStorybook
import com.umc.halo.domain.model.storybook.Storybook
import com.umc.halo.domain.model.storybook.StorybookTheme
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.Gray700
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloTheme
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary100
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.White

// 섹션 제목 색
private val SectionTitleColor = Color(0xFF3C3A35)

// 좌우 공통 가로 패딩
private val HorizontalPadding = 24.dp

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
        Spacer(Modifier.height(12.dp))

        StorybookSegmentedTabs(
            selectedTab = state.selectedTab,
            onTabSelected = { onEvent(StorybookUiEvent.OnTabSelected(it)) }
        )

        Spacer(Modifier.height(24.dp))

        when (state.selectedTab) {
            StorybookTab.ALL -> StorybookAllList(state = state, onEvent = onEvent)
            StorybookTab.IN_PROGRESS, StorybookTab.DONE -> ComingSoonPlaceholder() //여기는 추후 구현
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
            .height(36.dp)
            .clip(shape)
            .background(if (selected) Primary100 else White)
            .border(
                width = 1.dp,
                color = if (selected) Color.Transparent else Gray100,
                shape = shape
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = if (selected) HaloType.body03Medium else HaloType.body03Regular,
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
        contentPadding = PaddingValues(bottom = 30.dp),
        verticalArrangement = Arrangement.spacedBy(36.dp)
    ) {
        // 맞춤 스토리북
        if (state.customStorybooks.isNotEmpty()) {
            item {
                CustomStorybookSection(
                    userName = state.userName,
                    items = state.customStorybooks,
                    onClick = { onEvent(StorybookUiEvent.OnCustomStorybookClicked(it)) }
                )
            }
        }

        // 상황별 테마 섹션들
        items(
            items = state.themes,
            key = { it.id }
        ) { theme ->
            StorybookThemeSection(
                theme = theme,
                onClick = { onEvent(StorybookUiEvent.OnStorybookClicked(it)) }
            )
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
    Column {
        Column(modifier = Modifier.padding(horizontal = HorizontalPadding)) {
            Text(
                text = "${userName}님 맞춤 스토리북",
                style = HaloType.body01SemiBold,
                color = SectionTitleColor
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "부모님과의 관계를 개선하고 싶은 당신에게...",
                style = HaloType.caption01Regular,
                color = Gray400
            )
        }

        Spacer(Modifier.height(18.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = HorizontalPadding),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = items,
                key = { it.id }
            ) { item ->
                CustomStorybookCard(
                    item = item,
                    onClick = { onClick(item.id) }
                )
            }
        }
    }
}

@Composable
private fun CustomStorybookCard(
    item: CustomStorybook,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(246.dp)
            .clickable { onClick() },
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 커버 이미지 자리(현재는 임시) — 추후 구현 예정
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(108.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Gray100)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.tag,
                    style = HaloType.caption01Medium,
                    color = Primary500
                )
                Text(
                    text = item.title,
                    style = HaloType.body01SemiBold,
                    color = Gray800
                )
                Text(
                    text = item.subtitle,
                    style = HaloType.caption01Regular,
                    color = Gray500
                )
            }

            Icon(
                painter = painterResource(R.drawable.ic_home_right_arrow),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    }
}

/**
 * 상황별 테마 섹션
 */
@Composable
private fun StorybookThemeSection(
    theme: StorybookTheme,
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

        LazyRow(
            contentPadding = PaddingValues(horizontal = HorizontalPadding),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = theme.storybooks,
                key = { it.id }
            ) { book ->
                StorybookBookCard(
                    book = book,
                    onClick = { onClick(book.id) }
                )
            }
        }
    }
}

@Composable
private fun StorybookBookCard(
    book: Storybook,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(8.dp)
    Column(
        modifier = Modifier
            .width(131.dp)
            .height(166.dp)
            .shadow(elevation = 4.dp, shape = shape)
            .clip(shape)
            .background(White)
            .clickable { onClick() }
    ) {
        // 커버 이미지 자리(현재는 임시) — 추후 구현 예정
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(106.dp)
                .background(Gray100)
        )

        // 하단 흰색 라벨(제목/부제)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(White)
                .padding(horizontal = 10.dp, vertical = 11.dp)
        ) {
            Text(
                text = book.title,
                style = HaloType.body01SemiBold,
                color = Gray800,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = book.subtitle,
                style = HaloType.caption01Regular,
                color = Gray700
            )
        }
    }
}

/**
 * 진행중/완료 탭 임시 - 추후 구현 예정
 */
@Composable
private fun ComingSoonPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "준비 중",
            style = HaloType.body02Regular,
            color = Gray400
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun StorybookScreenPreview() {
    HaloTheme {
        StorybookScreen()
    }
}
