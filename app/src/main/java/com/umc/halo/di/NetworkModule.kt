package com.umc.halo.di

import com.umc.halo.BuildConfig
import com.umc.halo.core.network.AuthInterceptor
import com.umc.halo.core.network.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * 네트워크(OkHttp/Retrofit) 의존성을 Hilt 로 제공하는 모듈
 *
 * 사용법 : 생성자/필드에서 Retrofit(또는 OkHttpClient)을 @Inject 로 바로 받아 사용 가능
 *   예) class XxxApiService @Inject constructor(retrofit: Retrofit)
 *
 * baseUrl 은 BuildConfig.BASE_URL(= local.properties 의 BASE_URL) 을 사용
 *
 * 인증 처리는 두 단계로 나뉜다
 *   - AuthInterceptor    : 모든 요청에 Authorization 헤더를 붙임
 *   - TokenAuthenticator : 401(토큰 만료)이 오면 재발급 후 그 요청을 자동 재시도
 * 따라서 각 Api 인터페이스는 토큰을 신경 쓰지 않아도 됨
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            // 디버그 빌드에서만 요청/응답 본문 로깅 (릴리즈에선 로그 안 남김)
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
            // Bearer 토큰이 Logcat 에 그대로 찍히지 않도록 마스킹 처리
            redactHeader("Authorization")
            redactHeader("Cookie")
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient =
        OkHttpClient.Builder()
            // 순서 주의: 인증 헤더를 먼저 붙여야 로깅에도 (가려진 채로) 함께 남음
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(tokenAuthenticator)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}
