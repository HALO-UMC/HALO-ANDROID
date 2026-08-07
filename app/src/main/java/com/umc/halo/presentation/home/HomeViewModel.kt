package com.umc.halo.presentation.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.umc.halo.core.audio.BgmPlaybackManager
import com.umc.halo.R
import com.umc.halo.core.datastore.DeviceUuidDataStore
import com.umc.halo.data.remote.dto.request.notification.NotificationRequest
import com.umc.halo.domain.model.home.Books
import com.umc.halo.domain.model.home.HomeStatus
import com.umc.halo.domain.model.home.StartStorybook
import com.umc.halo.domain.model.home.UserInfo
import com.umc.halo.domain.model.home.UserState
import com.umc.halo.domain.repository.home.HomeRepository
import com.umc.halo.domain.repository.notification.NotificationRepository
import com.umc.halo.presentation.base.BaseViewModel
import com.umc.halo.presentation.component.storybookSpineOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val bgmPlaybackManager: BgmPlaybackManager
): BaseViewModel<HomeUiState, HomeUiEvent>(HomeUiState()) {
    val bgmState = bgmPlaybackManager.state

    override fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnBookClicked -> {
                selectedStorybook(event.storyBookId)
            }

            else -> Unit
        }
    }

    fun getHome() {
        viewModelScope.launch {
            val home = homeRepository.getHome()

            updateState {
                copy(
                    userInfo = UserInfo(home.memberName, true),
                    userState = if (home.homeStatus == HomeStatus.NO_STORYBOOK) UserState.FTU else UserState.RU,
                    bookShelf = home.bookShelfList,
                    customStorybookList = home.customStorybookList,
                    continueStorybookList = home.continueStorybookList
                )
            }
        }
    }

    fun loadBgmSetting() {
        viewModelScope.launch {
            bgmPlaybackManager.loadSettings()
        }
    }

    fun onBgmPlayerClicked() {
        viewModelScope.launch {
            bgmPlaybackManager.toggleHomePlayback()
        }
    }

    private fun selectedStorybook(id: Long?) {
        val book = _uiState.value.bookShelf.find { it.storybookId == id }

        Log.d("test",book.toString())

        if (book == null) {
            updateState {
                copy(
                    startStorybook = null
                )
            }
        } else {
            updateState {
                copy(
                    startStorybook = StartStorybook(
                        storybookId = book.storybookId,
                        title = book.title,
                        tag = book.recommendationReasonText,
                        currentProgress = book.currentChapterOrder,
                        isFirst = book.isFirst,
                        isCompleted = !book.todayAvailable,
                        imageUrl = book.imageUrl,
                        subtitle = book.shortDescription
                    )
                )
            }
        }
    }

    init {
        updateState {
            //프론트엔드 처리 데이터 (책장 데이터)
            copy(
                bookList = listOf(
                    Books(1, storybookSpineOf(1), R.drawable.image_common_bookcase_cover_1, 240, 48, 0f),
                    Books(2, storybookSpineOf(2), R.drawable.image_common_bookcase_cover_2, 230, 76, -14f),
                    Books(3, storybookSpineOf(3), R.drawable.image_common_bookcase_cover_3, 230, 50, 0f),
                    Books(4, storybookSpineOf(4), R.drawable.image_common_bookcase_cover_4, 230, 37, 3.6f),
                    Books(5, storybookSpineOf(5), R.drawable.image_common_bookcase_cover_5, 230, 46, 0f),
                    Books(6, storybookSpineOf(6), R.drawable.image_common_bookcase_cover_6, 230, 61, -6.2f),
                    Books(7, storybookSpineOf(7), R.drawable.image_common_bookcase_cover_7, 230, 52, 0f),
                    Books(8, storybookSpineOf(8), R.drawable.image_common_bookcase_cover_8, 230, 38, 0f),
                    Books(9, storybookSpineOf(9), R.drawable.image_common_bookcase_cover_9, 230, 64, 7.6f),
                    Books(10, storybookSpineOf(10), R.drawable.image_common_bookcase_cover_10, 230, 68, 0f)
                )
            )
        }
    }
}

