package com.umc.halo.core.network

import com.umc.halo.core.datastore.TokenDataStore
import com.umc.halo.data.remote.api.auth.AuthApi
import com.umc.halo.data.remote.dto.request.auth.ReissueRequest
import com.umc.halo.data.remote.dto.response.auth.ReissueResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

/** 재발급 시도의 결과 */
sealed interface ReissueResult {

    /**
     * 쓸 수 있는 accessToken 을 확보
     *
     * @param session 서버에 실제로 재발급을 요청해 받은 응답
     *                다른 요청이 이미 갱신해둬서 서버를 부르지 않았다면 null
     */
    data class Success(val accessToken: String, val session: ReissueResponse?) : ReissueResult

    /** refreshToken 이 만료, 무효(401) → 토큰 삭제됨. 재로그인 */
    data object Expired : ReissueResult

    /** 네트워크 단절, 타임아웃, 서버 오류 → 토큰은 그대로 두고 다음에 다시 시도 */
    data object Failed : ReissueResult
}

/**
 * 토큰 재발급을 앱 전체에서 한 번에 하나씩만 수행하는 단일점
 *
 * 재발급을 부르는 곳은 다음과 같이 두군데임
 * - [TokenAuthenticator] : 어떤 요청이든 401 을 받았을 때
 * - AuthRepositoryImpl.reissue() : 스플래시의 자동 로그인
 */
@Singleton
class TokenRefresher @Inject constructor(
    private val tokenDataStore: TokenDataStore,
    @param:ReissueClient private val authApi: AuthApi
) {

    private val mutex = Mutex()

    suspend fun refreshForRetry(): ReissueResult {
        val refreshTokenBeforeWait = tokenDataStore.refreshTokenFlow.first()

        return mutex.withLock {
            val refreshToken = tokenDataStore.refreshTokenFlow.first()

            if (refreshToken != null && refreshToken != refreshTokenBeforeWait) {
                val accessToken = tokenDataStore.accessTokenFlow.first()
                if (!accessToken.isNullOrBlank()) {
                    return@withLock ReissueResult.Success(accessToken, session = null)
                }
            }

            reissue(refreshToken)
        }
    }

    /**
     * 스플래시의 자동 로그인용 - 항상 서버에 재발급을 요청
     */
    suspend fun refreshForAutoLogin(): ReissueResult = mutex.withLock {
        reissue(tokenDataStore.refreshTokenFlow.first())
    }

    /**
     * 실제 재발급 요청. 반드시 [mutex] 안에서만 호출할 것
     */
    private suspend fun reissue(refreshToken: String?): ReissueResult {
        // 애초에 없으면 지울 것도 없음 (로그아웃 상태 등)
        if (refreshToken.isNullOrBlank()) return ReissueResult.Expired

        val response = runCatching { authApi.reissue(ReissueRequest(refreshToken)) }
            .getOrElse { throwable ->
                return if (throwable is HttpException && throwable.code() == HTTP_UNAUTHORIZED) {
                    // 401 = 이 refreshToken 은 죽음 -> 삭제 후 재로그인
                    tokenDataStore.clear()
                    ReissueResult.Expired
                } else {
                    // 그 외(IOException, 타임아웃, 5xx)는 토큰 보존
                    ReissueResult.Failed
                }
            }

        val result = response.result
        if (!response.isSuccess || result == null) {
            // 2xx 인데 실패로 온 경우 원인이 불분명하므로 토큰 삭제하지 않음
            return ReissueResult.Failed
        }

        // 서버가 refreshToken 도 회전시키므로 두 개를 함께 저장
        tokenDataStore.saveTokens(result.accessToken, result.refreshToken)
        return ReissueResult.Success(result.accessToken, result)
    }

    private companion object {
        const val HTTP_UNAUTHORIZED = 401
    }
}
