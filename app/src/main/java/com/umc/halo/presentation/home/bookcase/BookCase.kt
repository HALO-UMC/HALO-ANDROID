package com.umc.halo.presentation.home.bookcase

import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
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
    vm: HomeViewModel
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

        BookCaseContents(state.bookList,controller,vm)

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
    vm: HomeViewModel
) {
    var selectedId by remember {
        mutableStateOf<Int?>(null)
    }
    val listState = rememberLazyListState()

    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .height(246.dp),
        horizontalArrangement = Arrangement
            .spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        items(
            items = bookList,
            key = { item -> item.id }
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
                else
                    selectedId = item.id

                vm.onEvent(
                    HomeUiEvent.OnBookClicked(item.id)
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
        durationMillis = 600,
        easing = FastOutSlowInEasing
    )

    var startRotation by remember { mutableStateOf(false) }
    var resetTilt by remember { mutableStateOf(false) }
    LaunchedEffect(isSelected) {
        if (isSelected) {

            // tilt 제거
            resetTilt = true

            delay(300)

            // 책 펼치기
            startRotation = true

        } else {

            // 책 닫기
            startRotation = false

            delay(600)

            // tilt 복원
            resetTilt = false
        }
    }

    val spineRotation by animateFloatAsState(
        targetValue = if(startRotation) -90f else 0f,
        animationSpec = rotationSpec
    )
    val coverRotation by animateFloatAsState(
        targetValue = if (startRotation) 0f else 90f,
        animationSpec = rotationSpec
    )

    val spineAlpha = when {
            isSelected -> 1f
            !hasSelection -> 1f
            else -> 0.4f
        }

    val coverProgress = 1f - (coverRotation / 90f)
    val widthProgress = ((coverProgress - 0.8f) / 0.2f)
    val coverWidth = lerp(
        start = 0.dp,
        stop = 180.dp,
        fraction = coverProgress.coerceIn(0f, 1f)
    )
    val spineWidth = lerp(
        start = item.offsetX.dp,
        stop = 0.dp,
        fraction = widthProgress.coerceIn(0f, 1f)
    )

    val tilt by animateFloatAsState(
        targetValue = if (resetTilt) 0f else item.tilt,
        animationSpec = tween(300)
    )
    val offsetY by animateDpAsState(
        targetValue = if (resetTilt) 0.dp else item.offsetY.dp
    )

    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    LaunchedEffect(isSelected) {
        if (isSelected) {
            delay(150)
            bringIntoViewRequester.bringIntoView()
        }
    }

    Row(
        modifier = Modifier
            .bringIntoViewRequester(bringIntoViewRequester)
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .width(spineWidth)
                .height(item.height.dp)
                .offset(y = offsetY)
                .graphicsLayer {
                    transformOrigin = TransformOrigin(1f, 0.5f)
                    rotationY = spineRotation
                    rotationZ = tilt
                    cameraDistance = 16f
                    alpha = spineAlpha
                    clip = false
                }
                .background(Color.Transparent)
        ) {
            Image(
                painter = painterResource(item.spineImage),
                contentDescription = "${item.id}",
                modifier = Modifier.fillMaxSize()
            )
        }
        Box(
            modifier = Modifier
                .width(coverWidth)
                .height(item.height.dp)
                .graphicsLayer {
                    transformOrigin = TransformOrigin(0f, 0.5f)
                    rotationY = coverRotation
                    cameraDistance = 16f
                    clip = false
                }
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(White)
            ) {
                Image(
                    painter = painterResource(item.coverImage),
                    contentDescription = "${item.id}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .aspectRatio(180f/83f)
//                        .background(White)
//                        .padding(horizontal = 13.86.dp)
//                        .align(Alignment.BottomCenter),
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Spacer(Modifier.height(15.24.dp))
//
//                    Text(
//                        text = item.title,
//                        style = HaloType.heading01SemiBold,
//                        color = Gray800
//                    )
//
//                    Text(
//                        text = item.subtitle,
//                        style = HaloType.body01Regular,
//                        color = Gray700
//                    )
//                }
            }
        }
    }
}