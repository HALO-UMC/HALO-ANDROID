package com.umc.halo.core.network

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

/**
 * accessToken 만료(401)로 실패한 요청을 자동으로 되살리는 Authenticator
 */
@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenRefresher: TokenRefresher
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // 애초에 토큰을 안 붙였던 요청(login/reissue)이면 재발급으로 해결될 문제가 아님
        if (response.request.header(AuthInterceptor.HEADER_AUTHORIZATION) == null) return null

        // 재발급 후 재시도했는데 또 401 이면 여기서 멈춤 (무한 재시도 방지)
        if (responseCount(response) >= MAX_RETRY_COUNT) return null

        val newAccessToken = runBlocking {
            when (val result = tokenRefresher.refreshForRetry()) {
                is ReissueResult.Success -> result.accessToken
                // 만료(재로그인 필요)든 일시적 실패든 이 요청은 되살릴 수 없음
                ReissueResult.Expired, ReissueResult.Failed -> null
            }
        } ?: return null

        return response.request.newBuilder()
            .header(
                AuthInterceptor.HEADER_AUTHORIZATION,
                "${AuthInterceptor.BEARER_PREFIX}$newAccessToken"
            )
            .build()
    }

    // 같은 요청이 지금까지 몇 번 시도됐는지
    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }

    private companion object {
        const val MAX_RETRY_COUNT = 2
    }
}
