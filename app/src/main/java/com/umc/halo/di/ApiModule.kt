package com.umc.halo.di

import com.umc.halo.data.remote.api.auth.AuthApi
import com.umc.halo.data.remote.api.calendar.CalendarApi
import com.umc.halo.data.remote.api.home.HomeApi
import com.umc.halo.data.remote.api.member.MemberApi
import com.umc.halo.data.remote.api.notification.NotificationApi
import com.umc.halo.data.remote.api.onboarding.OnboardingApi
import com.umc.halo.data.remote.api.relationship.RelationshipApi
import com.umc.halo.data.remote.api.settings.SettingsApi
import com.umc.halo.data.remote.api.storybook.StorybookApi
import com.umc.halo.data.remote.api.storybook.StorybookDetailApi
import com.umc.halo.data.remote.api.terms.TermsApi
import com.umc.halo.data.remote.api.themebox.ThemeBoxApi
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
    fun provideCalendarApi(retrofit: Retrofit): CalendarApi =
        retrofit.create(CalendarApi::class.java)

    @Provides
    @Singleton
    fun provideHomeApi(retrofit: Retrofit): HomeApi =
        retrofit.create(HomeApi::class.java)

    @Provides
    @Singleton
    fun provideSettingsApi(retrofit: Retrofit): SettingsApi =
        retrofit.create(SettingsApi::class.java)

    @Provides
    @Singleton
    fun provideRelationshipApi(retrofit: Retrofit): RelationshipApi =
        retrofit.create(RelationshipApi::class.java)

    @Provides
    @Singleton
    fun provideStorybookDetailApi(retrofit: Retrofit): StorybookDetailApi =
        retrofit.create(StorybookDetailApi::class.java)

    @Provides
    @Singleton
    fun provideThemeBoxApi(retrofit: Retrofit): ThemeBoxApi =
        retrofit.create(ThemeBoxApi::class.java)

    @Provides
    @Singleton
    fun provideStorybookApi(retrofit: Retrofit): StorybookApi =
        retrofit.create(StorybookApi::class.java)

    @Provides
    @Singleton
    fun provideNotificationApi(retrofit: Retrofit): NotificationApi =
        retrofit.create(NotificationApi::class.java)
}
