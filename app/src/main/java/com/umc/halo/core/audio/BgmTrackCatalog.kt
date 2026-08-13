package com.umc.halo.core.audio

import androidx.annotation.RawRes
import com.umc.halo.R

data class BgmTrack(
    val id: Long,
    val title: String,
    @param:RawRes val rawResId: Int
)

object BgmTrackCatalog {
    const val DEFAULT_BGM_ID: Long = 1L

    val tracks: List<BgmTrack> = listOf(
        BgmTrack(id = 1L, title = "Morning Breeze", rawResId = R.raw.bgm_01),
        BgmTrack(id = 2L, title = "Warm Afternoon", rawResId = R.raw.bgm_02),
        BgmTrack(id = 3L, title = "Quiet Night", rawResId = R.raw.bgm_03)
    )

    fun trackById(id: Long?): BgmTrack = tracks.firstOrNull { it.id == id } ?: tracks.first()

    fun trackByIndex(index: Int): BgmTrack = tracks.getOrElse(index) { tracks.first() }

    fun indexOf(id: Long?): Int = tracks.indexOfFirst { it.id == id }.takeIf { it >= 0 } ?: 0
}
