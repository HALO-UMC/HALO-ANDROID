package com.umc.halo.core.network

import com.umc.halo.core.datastore.TokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 모든 요청에 `Authorization: Bearer {accessToken}` 헤더를 자동으로 붙이는 인터셉터
 *
 * 이게 없으면 인증이 필요한 API 마다 화면/Repository 에서 토큰을 꺼내 @Header 로 넘겨야 함
 * 약관·온보딩·로그아웃·탈퇴가 모두 인증을 요구하므로 한 곳에서 처리
 */
@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenDataStore: TokenDataStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // 토큰이 없거나 만료된 상태에서 부르는 API 라 헤더를 붙이면 안 됨
        if (request.url.encodedPath in NO_AUTH_PATHS) {
            return chain.proceed(request)
        }

        val accessToken = runBlocking { tokenDataStore.accessTokenFlow.first() }
            ?: return chain.proceed(request)

        val authorized = request.newBuilder()
            .header(HEADER_AUTHORIZATION, "$BEARER_PREFIX$accessToken")
            .build()

        return chain.proceed(authorized)
    }

    companion object {
        const val HEADER_AUTHORIZATION = "Authorization"
        const val BEARER_PREFIX = "Bearer "

        private val NO_AUTH_PATHS = setOf(
            "/api/v1/auth/login",
            "/api/v1/auth/reissue"
        )
    }
}
