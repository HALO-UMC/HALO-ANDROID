package com.umc.halo.di

import com.umc.halo.BuildConfig
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
 * 서버 주소가 아직 없어 local.properties 에 값이 없으면 build.gradle 의 placeholder(localhost)가 들어감
 * 실제 API 서비스(Api 인터페이스)와 DTO 는 서버 명세 확정 후 각 도메인에서 추가함
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
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
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
