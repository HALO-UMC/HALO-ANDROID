package com.umc.halo.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.umc.halo.R
import com.umc.halo.presentation.home.bookcase.BookCase
import com.umc.halo.presentation.home.customized_storybook.CustomizedStoryBook
import com.umc.halo.presentation.theme.Black
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.Gray600
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.White
import java.nio.file.WatchEvent

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}

@Composable
fun HomeScreen(
    vm: HomeViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
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
        modifier = Modifier.fillMaxWidth()
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

        CustomizedStoryBook(state.customizedStoryBookList)

        Spacer(Modifier.height(24.dp))

        ContinueStoryBook(state)
    }
}


@Composable
fun ContinueStoryBook(
    state: HomeUiState
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "${state.currentProgress}장을 바로 시작해보세요!",
            style = HaloType.body02Medium,
            color = Color(0xFF3C3A35)
        )

        Spacer(Modifier.height(12.dp))

        Box() {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(114.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                ContinueStoryBookContents()
            }

            ContentsOverlay()
        }
    }
}

@Composable
fun ContentsOverlay() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(114.dp)
            .alpha(0.9f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        )
    ) {
        Box(
            Modifier.fillMaxSize()
        )
        {
            Text(
                text = "테마 5장은\n'내일 다시' 참여할 수 있어요!",
                style = HaloType.body01Medium,
                color = Gray600,
                modifier = Modifier
                    .align(Alignment.Center),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ContinueStoryBookContents() {
    Row(
        modifier = Modifier
            .padding(
                horizontal = 18.dp,
                vertical = 12.dp
            )
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(75.dp)
                .border(
                    width = 0.dp,
                    color = Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                )
                .background(Color(0xF6F6F6))
        ) {

        }

        Spacer(Modifier.weight(2f))

        Column(
            modifier = Modifier
                .width(125.dp)
                .fillMaxHeight()
        ) {
            Spacer(Modifier.weight(2.5f))

            Text(
                text = "오래전 당신",
                style = HaloType.body01SemiBold,
                color = Black
            )

            Spacer(Modifier.weight(1f))

            Text(
                text = "오늘 6장까지 완료할 수 있어요!",
                style = HaloType.caption01Regular,
                color = Gray500
            )

            Spacer(Modifier.weight(6f))

            Text(
                text = "5/10",
                style = HaloType.caption01Regular,
                color = Primary500
            )

            Spacer(Modifier.weight(3f))

            Box(
                modifier = Modifier
                    .height(4.dp)
                    .width(121.dp)
                    .border(
                        width = 0.dp,
                        color = Color.Transparent,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .background(Gray100)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(54.dp)
                        .border(
                            width = 0.dp,
                            color = Color.Transparent,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .background(Primary500)
                )
            }

            Spacer(Modifier.weight(4f))
        }

        Spacer(Modifier.weight(5f))

        Icon(
            painter = painterResource(R.drawable.ic_home_right_arrow),
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}
