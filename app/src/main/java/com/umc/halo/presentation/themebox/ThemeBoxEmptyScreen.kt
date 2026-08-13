package com.umc.halo.presentation.themebox

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umc.halo.R
import com.umc.halo.domain.model.themebox.ContinueStorybook
import com.umc.halo.presentation.component.ContinueStorybookCard
import com.umc.halo.presentation.component.CustomStorybook
import com.umc.halo.presentation.home.ContentsOverlay
import com.umc.halo.presentation.home.HomeUiEvent
import com.umc.halo.presentation.theme.Gray30
import com.umc.halo.presentation.theme.Gray700
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType

@Composable
fun ThemeBoxEmptyScreen(
    state: ThemeBoxUiState,
    onEvent: (ThemeBoxUiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(Modifier.height(24.dp))

        ThemeBoxEmpty()

        Spacer(Modifier.height(35.dp))

        when (state.themeBoxState) {
            ThemeBoxState.Empty.FTU -> {
                CustomStorybook(state.customStorybookList) { id ->
                    onEvent(ThemeBoxUiEvent.OnCustomizedStoryBookClicked(id))
                }
            }
            ThemeBoxState.Empty.RU -> {
                ContinueStorybook(state.continueStorybookList, onEvent)
            }

            else -> Unit
        }
    }
}

@Composable
fun ThemeBoxEmpty() {
    Card(
        Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .aspectRatio(78f/47f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Gray30
        )
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier
                    .width(85.42.dp)
                    .height(99.29.dp),
                painter = painterResource(R.drawable.image_themebox_empty),
                contentDescription = null
            )

            Spacer(Modifier.height(18.dp))

            Text(
                text = "아직 완성된 캐릭터가 없어요!",
                style = HaloType.body02Medium,
                color = Gray800
            )
        }
    }
}

@Composable
fun ContinueStorybook(
    continueStorybookList: List<ContinueStorybook>,
    onEvent: (ThemeBoxUiEvent) -> Unit
) {
    LazyColumn(
        Modifier.padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "진행중인 스토리북 이어하기",
                style = HaloType.body01SemiBold,
                color = Gray700
            )

            Spacer(Modifier.height(18.dp))
        }

        items(
            items = continueStorybookList
        ) { item ->
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                ContinueStorybookCard(item) {
                    onEvent(ThemeBoxUiEvent.OnContinueStoryBookClicked(item.storybookId))
                }

                if (!item.todayAvailable) {
                    ContentsOverlay(item, Modifier.matchParentSize())
                }
            }
        }

        item {
            Spacer(Modifier.height(35.dp))
        }
    }

    Spacer(Modifier.height(38.dp))
}
