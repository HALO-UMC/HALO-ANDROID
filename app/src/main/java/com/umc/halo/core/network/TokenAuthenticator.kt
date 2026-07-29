package com.umc.halo.core.network

import com.umc.halo.core.datastore.TokenDataStore
import com.umc.halo.data.remote.api.auth.AuthApi
import com.umc.halo.data.remote.dto.request.auth.ReissueRequest
import dagger.Lazy
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

/**
 * accessToken 만료(401)로 실패한 요청을 자동으로 되살리는 Authenticator
 *
 * 흐름: 401 응답 → refreshToken 으로 재발급 → 새 토큰 저장 → 실패했던 요청을 새 토큰으로 재시도
 * 재발급까지 실패하면(refresh 도 만료) 저장된 토큰을 지우고 포기
 * 그러면 다음 앱 실행 때 스플래시가 토큰 없음으로 판단해 로그인 화면으로 보냄
 *
 * 토큰별 만료시잔은 Access - 1시간 / Refresh - 2주 이여서 앱을 오래 켜두면 이 경로로 진행됨
 *
 */
@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenDataStore: TokenDataStore,
    private val authApi: Lazy<AuthApi>
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // 애초에 토큰을 안 붙였던 요청(login/reissue)이면 재발급으로 해결될 문제가 아님
        if (response.request.header(AuthInterceptor.HEADER_AUTHORIZATION) == null) return null

        // 재발급 후 재시도했는데 또 401 이면 여기서 멈춤 (무한 재시도 방지)
        if (responseCount(response) >= MAX_RETRY_COUNT) return null

        val newAccessToken = runBlocking { reissueTokens() } ?: return null

        return response.request.newBuilder()
            .header(
                AuthInterceptor.HEADER_AUTHORIZATION,
                "${AuthInterceptor.BEARER_PREFIX}$newAccessToken"
            )
            .build()
    }

    /**
     * 재발급 성공 시 새 accessToken, 실패 시 null 을 반환하고 저장된 토큰을 정리
     * 서버가 refreshToken 도 함께 회전시키므로 두 토큰을 모두 새로 저장
     */
    private suspend fun reissueTokens(): String? {
        val refreshToken = tokenDataStore.refreshTokenFlow.first()
        if (refreshToken.isNullOrBlank()) {
            tokenDataStore.clear()
            return null
        }

        val result = runCatching { authApi.get().reissue(ReissueRequest(refreshToken)) }
            .getOrNull()
            ?.takeIf { it.isSuccess }
            ?.result

        if (result == null) {
            tokenDataStore.clear()
            return null
        }

        tokenDataStore.saveTokens(result.accessToken, result.refreshToken)
        return result.accessToken
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

    companion object {
        private const val MAX_RETRY_COUNT = 2
    }
}
