package com.umc.halo.presentation.home.bookcase

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.lottiefiles.dotlottie.core.compose.runtime.DotLottieController
import com.umc.halo.R
import com.umc.halo.domain.model.home.Books
import com.umc.halo.presentation.component.haloBookShadow
import com.umc.halo.presentation.component.haloCardShadow
import com.umc.halo.presentation.home.HomeUiEvent
import com.umc.halo.presentation.home.HomeUiState
import com.umc.halo.presentation.home.HomeViewModel
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray30
import com.umc.halo.presentation.theme.Gray700
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.White
import kotlinx.coroutines.delay


@Composable
fun BookCase(
    state: HomeUiState,
    controller: DotLottieController,
    onEvent: (HomeUiEvent) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8F5F3))
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(7.dp)
                .background(Color(0xFFDBCBC0))
        )

        BookCaseContents(state.bookList,controller,onEvent)

        Box(
            Modifier
                .fillMaxWidth()
                .height(7.dp)
                .background(Color(0xFFDBCBC0))
        )
    }
}

@Composable
fun BookCaseContents(
    bookList: List<Books>,
    controller: DotLottieController,
    onEvent: (HomeUiEvent) -> Unit
) {
    val listState = rememberLazyListState()
    var selectedId by remember { mutableStateOf<Long?>(null) }

    onEvent(
        HomeUiEvent.OnBookClicked(selectedId) // 화면 재구성시 StartStorybook 초기화 용도
    )

    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .height(246.dp)
            .background(Color(0xFFF8F5F3)),
        horizontalArrangement = Arrangement
            .spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom,
        userScrollEnabled = selectedId == null // 클릭 시 스크롤 막기
    ) {
        items(
            items = bookList
        ) { item ->
            val isSelected = item.id == selectedId

            BookItem(
                item = item,
                isSelected = isSelected,
                hasSelection = selectedId != null,
            ) {
                if(selectedId == null)
                    controller.play()

                if(selectedId == item.id)
                    selectedId = null
                else if (selectedId == null) {
                    selectedId = item.id
                }

                onEvent(
                    HomeUiEvent.OnBookClicked(selectedId)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookItem(
    item: Books,
    isSelected: Boolean,
    hasSelection: Boolean,
    onClick: () -> Unit
) {
    val rotationSpec = tween<Float>(
        durationMillis = 900,
        easing = FastOutSlowInEasing
    )

    var startRotation by remember { mutableStateOf(false) }
    var resetTilt by remember { mutableStateOf(false) }
    var changeAlpha by remember { mutableStateOf(false) }
    var deletePot by remember { mutableStateOf(false) }
    LaunchedEffect(hasSelection) {
        if (hasSelection) {
            // 화분 제거
            deletePot = true

            delay(100)

            // 선택된 책만 tilt 제거 + 회전
            if (isSelected) {
                // tilt 제거
                resetTilt = true

                delay(100)

                // 책 펼치기
                startRotation = true
            }

            delay(300)

            // 모든 아이템의 alpha 변경
            changeAlpha = true

        } else {

            if (!isSelected) {
                // 다른 책 alpha 복원 + 책 닫기
                startRotation = false
                changeAlpha = false

                delay(900)

                // tilt 복원
                resetTilt = false

                delay(100)
            }

            // 화분 복원
            deletePot = false
        }
    }

    val spineRotation by animateFloatAsState(
        targetValue = if(startRotation) -90f else 0f,
        animationSpec = rotationSpec
    )
    val coverRotation = spineRotation + 90f
    val targetSpineAlpha = when {
        isSelected -> 1f       // 내가 클릭한 책
        changeAlpha -> 0.4f     // 아무것도 선택 안 됨
        else -> 1f            // 다른 책
    }
    val spineAlpha by animateFloatAsState(
        targetValue = targetSpineAlpha,
        animationSpec = tween(300),
        label = "spineAlpha"
    )
    val potAlpha by animateFloatAsState(
        targetValue = if (deletePot) 0f else 1f,
        animationSpec = tween(100)
    )
    val width by animateDpAsState(
        targetValue = if (resetTilt) item.width.dp else item.offsetX.dp,
        animationSpec = tween(
            durationMillis = 100,
            easing = FastOutSlowInEasing
        )
    )
    val currentContentScale = if (width <= item.width.dp) {
        ContentScale.FillBounds
    } else {
        ContentScale.Fit
    }

    val openProgress = (-spineRotation / 90f).coerceIn(0f, 1f)
    val spineWidth = lerp(width, 0.dp, openProgress)
    val coverWidth = lerp(0.dp, 180.dp, openProgress)

    val tilt by animateFloatAsState(
        targetValue = if (resetTilt) 0f else item.tilt,
        animationSpec = tween(
            durationMillis = 100,
            easing = FastOutSlowInEasing
        )
    )
    val offsetY by animateDpAsState(
        targetValue = if (resetTilt) 0.dp else item.offsetY.dp,
        animationSpec = tween(
            durationMillis = 100,
            easing = FastOutSlowInEasing
        )
    )


    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    LaunchedEffect(isSelected) {
        if (isSelected) {
            delay(900)
            bringIntoViewRequester.bringIntoView()
        }
    }

    Row(
        modifier = Modifier
            .bringIntoViewRequester(bringIntoViewRequester)
            .fillMaxWidth()
            .then(
                if (!hasSelection || isSelected) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onClick()
                    }
                } else {
                    Modifier
                }
            )
            .padding(
                start = if (item.id == 1L) 24.dp else 0.dp,
                end = if (item.id == 10L) 24.dp else 0.dp
            )

    ) {
        Box(
            modifier = Modifier
                .width(spineWidth)
                .height(item.height.dp)
                .offset(y = offsetY)
                .graphicsLayer {
                    transformOrigin = TransformOrigin(1f, 0.5f)
                    rotationY = spineRotation
                    cameraDistance = 6f * density
                    alpha = spineAlpha
                    clip = false
                }
                .graphicsLayer(
                    transformOrigin = TransformOrigin(0.5f, 0.5f) ,
                    rotationZ = tilt
                )
                .background(Color.Transparent)
        ) {
            Image(
                painter = painterResource(item.spineImage),
                contentDescription = "${item.id}",
                modifier = Modifier.fillMaxSize(),
                contentScale = currentContentScale
            )

            if (item.id == 1L) {
                Icon(
                    painter = painterResource(R.drawable.ic_home_bookcase_pot),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .offset(
                            x = 35.dp,
                            y = 165.dp
                        )
                        .alpha(potAlpha)
                )
            }
        }
        Box(
            modifier = Modifier
                .width(coverWidth)
                .height(item.height.dp)
                .graphicsLayer {
                    transformOrigin = TransformOrigin(0f, 0.5f)
                    rotationY = coverRotation
                    cameraDistance = 6f * density
                    clip = false
                }
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(11.1.dp))
                    .haloCardShadow(RoundedCornerShape(11.1.dp))
            ) {
                Image(
                    painter = painterResource(item.coverImage),
                    contentDescription = "${item.id}",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }
}