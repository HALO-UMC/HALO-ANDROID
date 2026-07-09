package com.umc.halo.presentation.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.umc.halo.R
import com.umc.halo.presentation.home.bookcase.BookCase
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray30
import com.umc.halo.presentation.theme.HaloType

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(Modifier)
}

@Composable
fun HomeScreen(
    modifier: Modifier,
    vm: HomeViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            Modifier
                .fillMaxSize()
        ) {
            item {
                HomeScreenContents(state = state)
            }
        }
    }
}

@Composable
fun HomeScreenContents(
    state: HomeUiState
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(Modifier.height(40.dp))

        Text(
            text = "${state.name}님 반가워요!,\n${state.currentProgress}",
            style = HaloType.body01SemiBold,
            modifier = Modifier.padding(horizontal = 24.dp),
            color = Color(0xFF3C3A35)
        )

        Spacer(Modifier.height(85.dp))

        BookCase(state)

        Spacer(Modifier.height(21.dp))
    }
}
