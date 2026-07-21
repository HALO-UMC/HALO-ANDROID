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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.umc.halo.R
import com.umc.halo.presentation.component.ButtonState
import com.umc.halo.presentation.component.HaloMaterialButton
import com.umc.halo.presentation.home.HomeUiEvent
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
fun ThemeBoxScreen(
    vm: ThemeBoxViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()
    
    when (val uiState = state) {
        is ThemeBoxUiState.Filled -> {
            ThemeBoxFilledScreen(uiState)
        }
        is ThemeBoxUiState.Empty -> {
            ThemeBoxEmptyScreen(uiState, vm::onEvent)
        }
    }
}

@Composable
fun ThemeBoxFilledScreen(
    state: ThemeBoxUiState.Filled
) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(6f))
        //Spacer(Modifier.height(12.dp))

        ProgressBox(
            Modifier.weight(22f),
            state.numberOfCharacter,
            state.storyBookInProgress)

        Spacer(Modifier.weight(13f))
        //Spacer(Modifier.height(26.dp))

        ThemeBox(
            Modifier.weight(190f),
            state.themeList
        )

        Spacer(Modifier.weight(30f))
        //Spacer(Modifier.height(60.dp))

        HaloMaterialButton(
            buttonState = ButtonState.ABLE,
            text = "감상 시작",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .aspectRatio(52f/9f),
            style = HaloType.body01SemiBold
        ) {
            //네비게이션
        }

        Spacer(Modifier.weight(13f))
        //Spacer(Modifier.height(27.dp))
    }
}


@Composable
fun ThemeBoxEmptyScreen(
    state: ThemeBoxUiState.Empty,
    onEvent: (ThemeBoxUiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        Spacer(Modifier.height(24.dp))

        ThemeBoxEmpty()

        Spacer(Modifier.height(35.dp))

        when (state) {
            is ThemeBoxUiState.Empty.FTU -> {
                CustomStorybook(state.customStorybookList) {
                    //onClick 추가
                }
            }
            is ThemeBoxUiState.Empty.RU -> {
                ContinueStorybook(state.continueStorybookList)
            }
        }
    }
}

@Composable
fun ContinueStorybook(
    continueStorybookList: List<ContinueStorybook>
) {
    LazyColumn() {
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
            ContinueStorybookCard(item)
        }
    }
}

@Composable
fun ContinueStorybookCard(
    item: ContinueStorybook
) {
    Card(
        Modifier
            .fillMaxWidth()
            .aspectRatio(156f/37f)
            .background(White)
            .dropShadow(
                shape = RoundedCornerShape(12.dp),
                shadow = Shadow(
                    radius = 4.dp,
                    spread = 8.dp,
                    color = Color(0x1A858585),
                    offset = DpOffset(0.dp, 0.dp)
                )
            )
            .padding(
                horizontal = 18.dp,
                vertical = 16.dp)
    ) {
        Row {
            Column(
                Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    style = HaloType.body01SemiBold,
                    color = Gray800
                )

                Spacer(Modifier.weight(1f))

                Text(
                    text = "오늘 ${item.chapter}장까지 완료할 수 있어요!",
                    style = HaloType.caption01Regular,
                    color = Gray500
                )
            }

            Card(
                Modifier
                    .width(69.dp)
                    .height(36.dp)
                    .background(Primary30)
                    .padding(12.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row{
                    Text(
                        text = "${item.chapter}장",
                        style = HaloType.body02Medium,
                        color = Primary500
                    )

                    Spacer(Modifier.weight(1f))

                    Icon(
                        painter = painterResource(R.drawable.ic_continuestorybook_right_arrow),
                        contentDescription = null
                    )
                }
            }
        }

    }
}

@Composable
fun ThemeBoxEmpty() {
    Card(
        Modifier
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
fun ProgressBox(
    modifier: Modifier = Modifier,
    numberOfCharacter: Int,
    storyBookInProgress: Int
) {
    Row(
        modifier
            .padding(10.dp)
            .fillMaxWidth(14/20f)
            .height(IntrinsicSize.Min)
    ) {
        Column(
            Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "수집 캐릭터",
                style = HaloType.body03Medium,
                color = Gray600
            )

            Spacer(Modifier.weight(2f))
            //Spacer(Modifier.height(4.dp))

            Text(
                text = "${numberOfCharacter}개",
                style = HaloType.body01SemiBold,
                color = Gray800
            )
        }

        VerticalDivider(
            thickness = 1.dp,
            color = Gray100
        )

        Column(
            Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "진행중인 스토리북",
                style = HaloType.body03Medium,
                color = Gray600
            )

            Spacer(Modifier.weight(2f))
            //Spacer(Modifier.height(4.dp))

            Text(
                text = "${storyBookInProgress}개",
                style = HaloType.body01SemiBold,
                color = Gray800
            )
        }
    }
}

@Composable
fun ThemeBox(
    modifier: Modifier = Modifier,
    themeList: List<Theme>
) {
    Box(
        modifier
            .fillMaxWidth()
    ) {
        CarouselPager(themeList)
    }
}


@Preview(showBackground = true)
@Composable
fun ThemeBoxPreview() {
    ThemeBoxScreen()
}
