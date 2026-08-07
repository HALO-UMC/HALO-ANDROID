package com.umc.halo.di

import com.umc.halo.data.repository.anniversary.AnniversaryRepositoryImpl
import com.umc.halo.data.repository.auth.AuthRepositoryImpl
import com.umc.halo.data.repository.calendar.CalendarRepositoryImpl
import com.umc.halo.data.repository.chapter.ChapterRepositoryImpl
import com.umc.halo.data.repository.home.HomeRepositoryImpl
import com.umc.halo.data.repository.member.MemberRepositoryImpl
import com.umc.halo.data.repository.notification.NotificationRepositoryImpl
import com.umc.halo.data.repository.onboarding.OnboardingRepositoryImpl
import com.umc.halo.data.repository.relationship.RelationshipRepositoryImpl
import com.umc.halo.data.repository.settings.SettingsRepositoryImpl
import com.umc.halo.data.repository.storybook.StorybookDetailRepositoryImpl
import com.umc.halo.data.repository.storybook.StorybookRepositoryImpl
import com.umc.halo.data.repository.terms.TermsRepositoryImpl
import com.umc.halo.data.repository.themebox.ThemeBoxRepositoryImpl
import com.umc.halo.domain.repository.anniversary.AnniversaryRepository
import com.umc.halo.domain.repository.auth.AuthRepository
import com.umc.halo.domain.repository.calendar.CalendarRepository
import com.umc.halo.domain.repository.chapter.ChapterRepository
import com.umc.halo.domain.repository.home.HomeRepository
import com.umc.halo.domain.repository.member.MemberRepository
import com.umc.halo.domain.repository.notification.NotificationRepository
import com.umc.halo.domain.repository.onboarding.OnboardingRepository
import com.umc.halo.domain.repository.relationship.RelationshipRepository
import com.umc.halo.domain.repository.settings.SettingsRepository
import com.umc.halo.domain.repository.storybook.StorybookDetailRepository
import com.umc.halo.domain.repository.storybook.StorybookRepository
import com.umc.halo.domain.repository.terms.TermsRepository
import com.umc.halo.domain.repository.themebox.ThemeBoxRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Repository 인터페이스 ↔ 구현체 바인딩
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAnniversaryRepository(impl: AnniversaryRepositoryImpl): AnniversaryRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindMemberRepository(impl: MemberRepositoryImpl): MemberRepository

    @Binds
    @Singleton
    abstract fun bindTermsRepository(impl: TermsRepositoryImpl): TermsRepository

    @Binds
    @Singleton
    abstract fun bindOnboardingRepository(impl: OnboardingRepositoryImpl): OnboardingRepository

    @Binds
    @Singleton
    abstract fun bindCalendarRepository(impl: CalendarRepositoryImpl): CalendarRepository

    @Binds
    @Singleton
    abstract fun bindChapterRepository(impl: ChapterRepositoryImpl): ChapterRepository

    @Binds
    @Singleton
    abstract fun bindHomeRepository(impl: HomeRepositoryImpl): HomeRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindRelationshipRepository(impl: RelationshipRepositoryImpl): RelationshipRepository

    @Binds
    @Singleton
    abstract fun bindStorybookDetailRepository(impl: StorybookDetailRepositoryImpl): StorybookDetailRepository

    @Binds
    @Singleton
    abstract fun bindThemeBoxRepository(impl: ThemeBoxRepositoryImpl): ThemeBoxRepository

    @Binds
    @Singleton
    abstract fun bindStorybookRepository(impl: StorybookRepositoryImpl): StorybookRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(impl: NotificationRepositoryImpl): NotificationRepository
}
