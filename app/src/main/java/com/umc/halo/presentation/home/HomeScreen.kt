package com.umc.halo.presentation.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.messaging.FirebaseMessaging
import com.lottiefiles.dotlottie.core.compose.runtime.DotLottieController
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource
import com.umc.halo.core.audio.BgmPlaybackState
import com.umc.halo.domain.model.home.UserState
import com.umc.halo.presentation.home.actionguide.ActionGuide
import com.umc.halo.presentation.home.bookcase.BookCase
import com.umc.halo.presentation.component.CustomStorybook
import com.umc.halo.presentation.storybook.list.StorybookUiEvent
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary30
import com.umc.halo.presentation.theme.Primary500
import java.util.UUID

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToStorybook: (Long) -> Unit
) {
    LaunchedEffect(Unit) {
        //화면 불러오기
        viewModel.loadHome()
    }


    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()
    val bgmState by viewModel.bgmState.collectAsState()

    LaunchedEffect(state.errorMessage) {
        val message = state.errorMessage ?: return@LaunchedEffect
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        viewModel.onEvent(HomeUiEvent.ErrorShown)
    }

    HomeScreen(
        state = state,
        bgmState = bgmState,
        onBgmClick = viewModel::onBgmPlayerClicked,
        onEvent = { event ->
            when (event) {
                is HomeUiEvent.OnBookClicked -> {
                    viewModel.onEvent(event)
                }

                is HomeUiEvent.ErrorShown -> {
                    viewModel.onEvent(event)
                }

                is HomeUiEvent.OnCustomizedStoryBookClicked -> {
                    onNavigateToStorybook(event.storyBookId)
                }

                is HomeUiEvent.OnContinueStoryBookClicked -> {
                    onNavigateToStorybook(event.storyBookId)
                }

                is HomeUiEvent.OnStartStorybookClicked -> {
                    onNavigateToStorybook(event.storyBookId)
                }
            }
        }
    )
}

@Composable
fun HomeScreen(
    state: HomeUiState,
    bgmState: BgmPlaybackState,
    onBgmClick: () -> Unit,
    onEvent: (HomeUiEvent) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            Modifier
                .fillMaxSize()
        ) {
            item {
                HomeScreenContents(
                    state = state,
                    bgmState = bgmState,
                    onBgmClick = onBgmClick,
                    onEvent = onEvent
                )
            }
        }
    }
}

@Composable
fun HomeScreenContents(
    state: HomeUiState,
    bgmState: BgmPlaybackState,
    onBgmClick: () -> Unit,
    onEvent: (HomeUiEvent) -> Unit
) {
    val controller = remember { DotLottieController() }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (bgmState.bgmEnabled) {
            Spacer(Modifier.height(18.dp))

            BackGroundMusicPlayer(
                title = bgmState.title,
                isPlaying = bgmState.isPlaying,
                onClick = onBgmClick
            )

            Spacer(Modifier.height(28.dp))
        } else {
            Spacer(Modifier.height(46.dp))
        }

        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(color = Primary500)
                ) {
                    append("${state.userInfo.name} ")
                }
                append("안녕하세요!,\n스토리북 작성을 시작해보세요!")
            },
            style = HaloType.heading03Medium,
            modifier = Modifier.padding(horizontal = 24.dp),
            color = Color(0xFF3C3A35)
        )

        Spacer(Modifier.height(57.dp))

        Box {
            BookCase(state,controller, onEvent)

            Box(
                modifier = Modifier
                    .offset(y = (-80).dp)
                    .align(Alignment.TopEnd)
                    .padding(horizontal = 20.dp)
                    .width(90.dp)
                    .height(100.dp)
            ) {
                DotLottieAnimation(
                    source = DotLottieSource.Asset("main_charactermotion.lottie"),
                    controller = controller,
                    autoplay = false,
                    loop = false
                )
            }
        }

        when (state.userState) {
            UserState.FTU -> {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(Primary30)
                        .padding(vertical = 23.dp)
                ) {
                    if (state.startStorybook != null) {
                        StartStorybook(state.startStorybook, onEvent)
                    } else {
                        val customStorybook = state.customStorybookList
                        CustomStorybook(customStorybook,
                            onClick = { id ->
                                onEvent(HomeUiEvent.OnCustomizedStoryBookClicked(id))
                            }
                        )
                    }
                }

                Spacer(Modifier.height(32.dp))

                ActionGuide()
            }

            is UserState.RU -> {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(Primary30)
                        .padding(vertical = 23.dp)
                ) {
                    if (state.startStorybook != null) {
                        StartStorybook(state.startStorybook, onEvent)
                    } else {
                        ContinueStorybookHome(state.continueStorybookList, onEvent)
                    }
                }

                Spacer(Modifier.height(32.dp))

                ActionGuide()
            }
        }

        Spacer(Modifier.height(30.dp))
    }
}





