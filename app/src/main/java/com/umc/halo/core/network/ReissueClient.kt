package com.umc.halo.core.network

import javax.inject.Qualifier

/**
 * 토큰 재발급(POST /auth/reissue) 전용 한정자
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ReissueClient
