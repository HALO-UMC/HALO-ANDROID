package com.umc.halo.core.audio

import android.content.Context
import android.net.Uri
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BgmPlayerController @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private var player: ExoPlayer? = null
    private var currentBgmId: Long? = null
    private var currentVolume: Float = DEFAULT_VOLUME

    val playingBgmId: Long?
        get() = currentBgmId.takeIf { isPlaying }

    val isPlaying: Boolean
        get() = player?.isPlaying == true

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

        val currentPlayer = player ?: createPlayer().also { player = it }
        if (currentBgmId != track.id) {
            currentPlayer.setMediaItem(MediaItem.fromUri(track.rawUri()))
            currentPlayer.prepare()
            currentBgmId = track.id
        }

        currentPlayer.volume = currentVolume
        currentPlayer.playWhenReady = true
        currentPlayer.play()
    }

    fun pause() {
        player?.pause()
    }

    fun stop() {
        player?.stop()
        currentBgmId = null
    }

    fun setVolume(volume: Float) {
        currentVolume = volume.coerceIn(0f, 1f)
        player?.volume = currentVolume
    }

    private fun createPlayer(): ExoPlayer {
        return ExoPlayer.Builder(context)
            .build()
            .apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(C.USAGE_MEDIA)
                        .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                        .build(),
                    true
                )
                repeatMode = Player.REPEAT_MODE_ONE
                volume = currentVolume
            }
    }

    private fun BgmTrack.rawUri(): Uri =
        Uri.parse("android.resource://${context.packageName}/$rawResId")

    private companion object {
        const val DEFAULT_VOLUME = 0.5f
    }
}
