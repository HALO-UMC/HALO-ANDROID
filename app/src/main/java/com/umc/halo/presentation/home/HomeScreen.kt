package com.umc.halo.presentation.home

import android.graphics.Paint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.umc.halo.R
import com.umc.halo.presentation.home.actionguide.ActionGuide
import com.umc.halo.presentation.home.bookcase.BookCase
import com.umc.halo.presentation.home.continue_storybook.ContinueStoryBook
import com.umc.halo.presentation.home.customized_storybook.CustomizedStoryBook
import com.umc.halo.presentation.theme.Gray200
import com.umc.halo.presentation.theme.Gray30
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.Gray600
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary30
import com.umc.halo.presentation.theme.Primary50

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

        if (state.storyBookState == StoryBookState.BeforeStart) {
            CustomizedStoryBook(state.customizedStoryBookList)

            Spacer(Modifier.height(32.dp))

            ActionGuide()
        }

        Spacer(Modifier.height(24.dp))

        if (state.storyBookState != StoryBookState.BeforeStart)
            ContinueStoryBook(state)

        Spacer(Modifier.height(30.dp))
    }
}


