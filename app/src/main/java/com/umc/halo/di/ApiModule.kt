package com.umc.halo.di

import com.umc.halo.data.remote.api.auth.AuthApi
import com.umc.halo.data.remote.api.home.HomeApi
import com.umc.halo.data.remote.api.member.MemberApi
import com.umc.halo.data.remote.api.onboarding.OnboardingApi
import com.umc.halo.data.remote.api.terms.TermsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Retrofit 으로 각 도메인 Api 인터페이스를 생성해 제공
 * 새 Api 가 생기면 여기에 provideXxxApi 를 추가
 */
@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideMemberApi(retrofit: Retrofit): MemberApi =
        retrofit.create(MemberApi::class.java)

    @Provides
    @Singleton
    fun provideTermsApi(retrofit: Retrofit): TermsApi =
        retrofit.create(TermsApi::class.java)

    @Provides
    @Singleton
    fun provideOnboardingApi(retrofit: Retrofit): OnboardingApi =
        retrofit.create(OnboardingApi::class.java)

    @Provides
    @Singleton
    fun provideHomeApi(retrofit: Retrofit): HomeApi =
        retrofit.create(HomeApi::class.java)
}
