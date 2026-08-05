package com.umc.halo.core.audio

import android.content.Context
import android.media.MediaPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BgmPlayerController @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private var mediaPlayer: MediaPlayer? = null
    private var currentBgmId: Long? = null
    private var currentVolume: Float = DEFAULT_VOLUME

    val playingBgmId: Long?
        get() = currentBgmId.takeIf { isPlaying }

    val isPlaying: Boolean
        get() = runCatching { mediaPlayer?.isPlaying == true }.getOrDefault(false)

    fun applySettings(bgmId: Long?, enabled: Boolean, volume: Float) {
        currentVolume = volume.coerceIn(0f, 1f)
        if (enabled) {
            play(bgmId ?: BgmTrackCatalog.DEFAULT_BGM_ID, currentVolume)
        } else {
            stop()
        }
    }

    fun play(bgmId: Long, volume: Float = currentVolume) {
        val track = BgmTrackCatalog.trackById(bgmId)
        currentVolume = volume.coerceIn(0f, 1f)

        if (currentBgmId != track.id || mediaPlayer == null) {
            releasePlayer()
            mediaPlayer = createPlayer(track)
            currentBgmId = track.id
        }

        startPlayer(track)
    }

    fun pause() {
        runCatching {
            mediaPlayer?.takeIf { it.isPlaying }?.pause()
        }
    }

    fun stop() {
        runCatching {
            mediaPlayer?.let { player ->
                if (player.isPlaying) player.pause()
                player.seekTo(0)
            }
        }
    }

    fun setVolume(volume: Float) {
        currentVolume = volume.coerceIn(0f, 1f)
        runCatching {
            mediaPlayer?.setVolume(currentVolume, currentVolume)
        }
    }

    private fun createPlayer(track: BgmTrack): MediaPlayer? {
        return MediaPlayer.create(context, track.rawResId)?.apply {
            isLooping = true
            setVolume(currentVolume, currentVolume)
            setOnCompletionListener { player ->
                runCatching {
                    player.seekTo(0)
                    player.start()
                }.onFailure {
                    releasePlayer()
                }
            }
            setOnErrorListener { _, _, _ ->
                releasePlayer()
                true
            }
        }
    }

    private fun startPlayer(track: BgmTrack) {
        val player = mediaPlayer ?: run {
            mediaPlayer = createPlayer(track)
            currentBgmId = track.id
            mediaPlayer
        }

        val started = runCatching {
            player?.setVolume(currentVolume, currentVolume)
            if (player?.isPlaying != true) {
                player?.start()
            }
        }.isSuccess

        if (!started) {
            releasePlayer()
            mediaPlayer = createPlayer(track)
            currentBgmId = track.id
            runCatching {
                mediaPlayer?.start()
            }
        }
    }

    private fun releasePlayer() {
        runCatching {
            mediaPlayer?.release()
        }
        mediaPlayer = null
        currentBgmId = null
    }

    private companion object {
        const val DEFAULT_VOLUME = 0.7f
    }
}
