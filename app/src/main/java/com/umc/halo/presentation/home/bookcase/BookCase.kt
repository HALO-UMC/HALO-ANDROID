package com.umc.halo.presentation.home.bookcase

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.lottiefiles.dotlottie.core.compose.runtime.DotLottieController
import com.umc.halo.R
import com.umc.halo.domain.model.home.Books
import com.umc.halo.presentation.home.HomeUiEvent
import com.umc.halo.presentation.home.HomeUiState
import com.umc.halo.presentation.home.HomeViewModel
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray30
import com.umc.halo.presentation.theme.HaloType


@Composable
fun BookCase(
    state: HomeUiState,
    controller: DotLottieController,
    vm: HomeViewModel
) {
    Column(
        Modifier
            .fillMaxWidth()
            .height(210.dp)
            .border(
                width = 1.dp,
                color = Gray100
            )
            .background(Gray30)
    ) {
        Spacer(Modifier.height(12.dp))

        Text(
            text = "책장 둘러보기",
            style = HaloType.body01SemiBold,
            modifier = Modifier.padding(horizontal = 24.dp),
            color = Color(0xFF3C3A35)
        )

        Spacer(Modifier.height(24.dp))

        Box() {
            Image(
                painter = painterResource(R.drawable.shape_home_shelf),
                contentDescription = null,
                modifier = Modifier
                    .offset(y = (120).dp)
                    .fillMaxWidth()
                    .dropShadow(
                        shape = RoundedCornerShape(8.dp),
                        shadow = Shadow(
                            radius = 4.dp,
                            spread = 0.dp,
                            color = Color(0x4D3E3E3E),
                            offset = DpOffset(0.dp, 2.dp)
                        )
                    ),
                contentScale = ContentScale.FillBounds
            )

            BookCaseContents(state.bookList,controller,vm)
        }
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

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(125.dp)
            .padding(horizontal = 24.dp),
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
            ) {
                if(selectedId == null)
                    controller.play()

                //-- 토글 로직 제거 (논의 중)
                if(selectedId == item.id)
                    //selectedId = null
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
    onClick: () -> Unit
) {
    val spineRotation by animateFloatAsState(targetValue = if(isSelected) 90f else 0f)
    val coverRotation by animateFloatAsState(targetValue = if (isSelected) 0f else 90f)

    val spineAlpha by animateFloatAsState(targetValue = if (isSelected) 0f else 1f, label = "spineAlpha")
    val coverAlpha by animateFloatAsState(targetValue = if (isSelected) 1f else 0f, label = "coverAlpha")

    val coverSize by animateDpAsState(targetValue = if (isSelected) 100.dp else 0.dp)
    val spineSize by animateDpAsState(targetValue = if (isSelected) 0.dp else item.size.width.dp)

    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    LaunchedEffect(coverSize) {
        if (isSelected) {
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
                .width(spineSize)
                .height(item.size.height.dp)
                .graphicsLayer {
                    transformOrigin = TransformOrigin(1f, 0.5f)
                    rotationY = spineRotation
                    cameraDistance = 16f
                    alpha = spineAlpha
                    clip = false
                }
                .background(item.color)

        )
        Box(
            modifier = Modifier
                .width(coverSize)
                .height(item.size.height.dp)
                .graphicsLayer {
                    transformOrigin = TransformOrigin(0f, 0.5f)
                    rotationY = coverRotation
                    cameraDistance = 16f
                    alpha = coverAlpha
                    clip = false
                }
                .background(item.color)
        )
    }
}