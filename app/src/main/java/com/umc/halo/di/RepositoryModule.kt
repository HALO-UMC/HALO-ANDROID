package com.umc.halo.di

import com.umc.halo.data.repository.auth.AuthRepositoryImpl
import com.umc.halo.data.repository.calendar.CalendarRepositoryImpl
import com.umc.halo.data.repository.home.HomeRepositoryImpl
import com.umc.halo.data.repository.member.MemberRepositoryImpl
import com.umc.halo.data.repository.onboarding.OnboardingRepositoryImpl
import com.umc.halo.data.repository.storybook.StorybookDetailRepositoryImpl
import com.umc.halo.data.repository.terms.TermsRepositoryImpl
import com.umc.halo.domain.repository.auth.AuthRepository
import com.umc.halo.domain.repository.calendar.CalendarRepository
import com.umc.halo.domain.repository.home.HomeRepository
import com.umc.halo.domain.repository.member.MemberRepository
import com.umc.halo.domain.repository.onboarding.OnboardingRepository
import com.umc.halo.domain.repository.storybook.StorybookDetailRepository
import com.umc.halo.domain.repository.terms.TermsRepository
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
    abstract fun bindHomeRepository(impl: HomeRepositoryImpl): HomeRepository

    @Binds
    @Singleton
    abstract fun bindStorybookDetailRepository(impl: StorybookDetailRepositoryImpl): StorybookDetailRepository
}
