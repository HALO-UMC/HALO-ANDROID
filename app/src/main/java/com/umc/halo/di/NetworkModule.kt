package com.umc.halo.di

import com.umc.halo.BuildConfig
import com.umc.halo.core.network.AuthInterceptor
import com.umc.halo.core.network.ReissueClient
import com.umc.halo.core.network.TokenAuthenticator
import com.umc.halo.data.remote.api.auth.AuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * 네트워크(OkHttp/Retrofit) 의존성을 Hilt 로 제공하는 모듈
 *
 * 사용법 : 생성자/필드에서 Retrofit(또는 OkHttpClient)을 @Inject 로 바로 받아 사용 가능
 *   예) class XxxApiService @Inject constructor(retrofit: Retrofit)
 *
 * baseUrl 은 BuildConfig.BASE_URL 을 사용
 *   값은 app/build.gradle.kts 의 buildTypes 에 직접 적혀 있음 (debug=개발 서버 / release=운영 서버)
 *
 * 인증 처리는 두 단계로 나뉜다
 *   - AuthInterceptor    : 모든 요청에 Authorization 헤더를 붙임
 *   - TokenAuthenticator : 401(토큰 만료)이 오면 재발급 후 그 요청을 자동 재시도
 * 따라서 각 Api 인터페이스는 토큰을 신경 쓰지 않아도 됨
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // 요청 1건의 연결, 읽기, 쓰기 제한
    // 기본값(10초)보다 조금 넉넉하게 잡음
    private const val TIMEOUT_SECONDS = 15L

    /**
     * 호출 하나 전체의 제한 - 재발급과 재시도까지 포함한 시간
     */
    private const val CALL_TIMEOUT_SECONDS = 60L

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
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .callTimeout(CALL_TIMEOUT_SECONDS, TimeUnit.SECONDS)
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

    // -------------------------------------------
    // 토큰 재발급 전용 (일반 요청과 분리함)
    //
    // AuthApi 지만 ApiModule 이 아니라 여기에 둠
    // -------------------------------------------

    /**
     * AuthInterceptor 도 TokenAuthenticator 도 붙이지 않음
     * - 재발급 요청에는 Authorization 헤더가 필요 없고 재발급이 401 을 받았을 때 또 재발급을 시도하면 무한 루프가 됨
     */
    @Provides
    @Singleton
    @ReissueClient
    fun provideReissueOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .callTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    @ReissueClient
    fun provideReissueRetrofit(
        @ReissueClient okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @ReissueClient
    fun provideReissueAuthApi(
        @ReissueClient retrofit: Retrofit
    ): AuthApi = retrofit.create(AuthApi::class.java)
}
