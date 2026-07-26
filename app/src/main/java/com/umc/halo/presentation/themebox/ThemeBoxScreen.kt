package com.umc.halo.presentation.themebox

import android.annotation.SuppressLint
import androidx.appcompat.widget.DialogTitle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.umc.halo.R
import com.umc.halo.domain.model.themebox.ContinueStorybook
import com.umc.halo.domain.model.themebox.Theme
import com.umc.halo.presentation.component.ButtonState
import com.umc.halo.presentation.component.ContinueStorybookCard
import com.umc.halo.presentation.component.HaloMaterialButton
import com.umc.halo.presentation.home.HomeScreen
import com.umc.halo.presentation.home.HomeUiEvent
import com.umc.halo.presentation.home.HomeViewModel
import com.umc.halo.presentation.home.custom_storybook.CustomStorybook
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray30
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.Gray600
import com.umc.halo.presentation.theme.Gray700
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary30
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.White
import kotlin.math.absoluteValue

@Composable
fun ThemeBoxRoute(
    viewModel: ThemeBoxViewModel = hiltViewModel(),
    onNavigateToStorybook: (Long) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    ThemeBoxScreen(
        state = state,
        onEvent = { event ->
            when (event) {
                is ThemeBoxUiEvent.OnContinueStoryBookClicked -> {
                    onNavigateToStorybook(event.storyBookId)
                }

                is ThemeBoxUiEvent.OnCustomizedStoryBookClicked -> {
                    onNavigateToStorybook(event.storyBookId)
                }
            }
        }
    )
}

@Composable
fun ThemeBoxScreen(
    state: ThemeBoxUiState,
    onEvent: (ThemeBoxUiEvent) -> Unit
) {
    when (state) {
        is ThemeBoxUiState.Filled -> {
            ThemeBoxFilledScreen(state)
        }
        is ThemeBoxUiState.Empty -> {
            ThemeBoxEmptyScreen(state, onEvent)
        }
    }
}
