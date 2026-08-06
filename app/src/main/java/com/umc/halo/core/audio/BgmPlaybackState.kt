package com.umc.halo.core.audio

data class BgmPlaybackState(
    val bgmEnabled: Boolean = false,
    val bgmId: Long = BgmTrackCatalog.DEFAULT_BGM_ID,
    val volume: Float = DEFAULT_VOLUME,
    val isPlaying: Boolean = false,
    val isLoading: Boolean = false
) {
    val selectedTrackIndex: Int
        get() = BgmTrackCatalog.indexOf(bgmId)

    val playingTrackIndex: Int?
        get() = selectedTrackIndex.takeIf { isPlaying }

    val title: String
        get() = BgmTrackCatalog.trackById(bgmId).title

    companion object {
        const val DEFAULT_VOLUME = 0.5f
    }
}
