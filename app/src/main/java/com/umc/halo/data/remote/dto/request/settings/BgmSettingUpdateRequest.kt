package com.umc.halo.data.remote.dto.request.settings

data class BgmSettingUpdateRequest(
    val bgmId: Long?,
    val bgmEnabled: Boolean,
    val bgmVolume: Int
)
