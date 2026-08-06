package com.umc.halo.core.audio

import android.content.Context
import com.umc.halo.domain.model.settings.BgmSetting
import com.umc.halo.domain.repository.settings.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

@Singleton
class BgmPlaybackManager @Inject constructor(
    @param:ApplicationContext context: Context,
    private val settingsRepository: SettingsRepository,
    private val playerController: BgmPlayerController
) {
    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val _state = MutableStateFlow(BgmPlaybackState())
    val state: StateFlow<BgmPlaybackState> = _state.asStateFlow()

    private var loaded = false

    suspend fun loadSettings(force: Boolean = false): Result<Unit> {
        if (loaded && !force) return Result.success(Unit)

        _state.update { it.copy(isLoading = true) }

        return runCatching { settingsRepository.getBgmSetting() }
            .mapCatching { setting ->
                loaded = true
                applySetting(setting, syncPlayer = true)
            }
            .onFailure {
                playerController.stop()
                _state.update { current ->
                    current.copy(
                        isLoading = false,
                        bgmEnabled = false,
                        isPlaying = false
                    )
                }
            }
    }

    suspend fun setEnabled(enabled: Boolean): Result<Unit> {
        val current = _state.value

        if (enabled) {
            playerController.play(current.bgmId, current.volume)
            saveShouldResumePlayback(true)
        } else {
            playerController.stop()
            saveShouldResumePlayback(false)
        }

        _state.update {
            it.copy(
                bgmEnabled = enabled,
                isPlaying = enabled
            )
        }

        return saveCurrent()
    }

    fun stopForAppExit() {
        playerController.stop()
        _state.update { it.copy(isPlaying = false) }
    }

    fun setVolume(volume: Float) {
        val coercedVolume = volume.coerceIn(0f, 1f)
        playerController.setVolume(coercedVolume)
        _state.update { it.copy(volume = coercedVolume) }
    }

    suspend fun saveCurrent(): Result<Unit> {
        val current = _state.value
        val setting = BgmSetting(
            bgmId = current.bgmId,
            bgmEnabled = current.bgmEnabled,
            bgmVolume = current.volume.toVolumeInt()
        )

        val wasPlaying = current.isPlaying

        return runCatching { settingsRepository.updateBgmSetting(setting) }
            .mapCatching { updatedSetting ->
                applySetting(
                    setting = updatedSetting,
                    syncPlayer = false,
                    keepPlayingState = wasPlaying
                )
            }
    }

    suspend fun onTrackClicked(index: Int): Result<Unit> {
        val current = _state.value
        val track = BgmTrackCatalog.trackByIndex(index)
        val isSameTrack = current.bgmId == track.id

        if (isSameTrack && current.bgmEnabled) {
            return togglePlaybackOnly()
        }

        if (isSameTrack) {
            return setEnabled(true)
        }

        playerController.play(track.id, current.volume)
        saveShouldResumePlayback(true)
        _state.update {
            it.copy(
                bgmEnabled = true,
                bgmId = track.id,
                isPlaying = true
            )
        }

        return saveCurrent()
    }

    suspend fun toggleHomePlayback(): Result<Unit> {
        val current = _state.value
        return if (current.bgmEnabled) {
            togglePlaybackOnly()
        } else {
            setEnabled(true)
        }
    }

    private fun togglePlaybackOnly(): Result<Unit> {
        val current = _state.value
        if (current.isPlaying) {
            playerController.pause()
            saveShouldResumePlayback(false)
            _state.update { it.copy(isPlaying = false) }
        } else {
            playerController.play(current.bgmId, current.volume)
            saveShouldResumePlayback(true)
            _state.update {
                it.copy(
                    bgmEnabled = true,
                    isPlaying = true
                )
            }
        }
        return Result.success(Unit)
    }

    private fun applySetting(
        setting: BgmSetting,
        syncPlayer: Boolean,
        keepPlayingState: Boolean? = null
    ) {
        val volume = setting.bgmVolume.toVolumeFloat()
        val bgmId = BgmTrackCatalog.trackById(setting.bgmId).id
        val shouldResumePlayback = shouldResumePlayback()
        val isPlaying = keepPlayingState ?: (setting.bgmEnabled && shouldResumePlayback)

        if (syncPlayer) {
            playerController.applySettings(
                bgmId = bgmId,
                enabled = isPlaying,
                volume = volume
            )
        }

        _state.update {
            it.copy(
                isLoading = false,
                bgmEnabled = setting.bgmEnabled,
                bgmId = bgmId,
                volume = volume,
                isPlaying = isPlaying
            )
        }
    }

    private fun shouldResumePlayback(): Boolean =
        preferences.getBoolean(KEY_SHOULD_RESUME_PLAYBACK, true)

    private fun saveShouldResumePlayback(shouldResume: Boolean) {
        preferences.edit()
            .putBoolean(KEY_SHOULD_RESUME_PLAYBACK, shouldResume)
            .apply()
    }

    private companion object {
        const val PREFERENCES_NAME = "bgm_playback_preferences"
        const val KEY_SHOULD_RESUME_PLAYBACK = "should_resume_playback"
    }
}

private fun Int.toVolumeFloat(): Float = coerceIn(0, 100) / 100f

private fun Float.toVolumeInt(): Int = (coerceIn(0f, 1f) * 100).roundToInt().coerceIn(0, 100)
