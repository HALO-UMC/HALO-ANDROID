package com.umc.halo.di

import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.umc.halo.core.logging.AnalyticsLogger
import com.umc.halo.core.logging.CrashlyticsLogger
import com.umc.halo.core.logging.FireBaseAnalyticsLogger
import com.umc.halo.core.logging.FireBaseCrashlyticsLogger
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LoggingModule {

    @Binds
    @Singleton
    abstract fun bindAnalyticsLogger(impl: FireBaseAnalyticsLogger): AnalyticsLogger

    @Binds
    @Singleton
    abstract fun bindCrashlyticsLogger(impl: FireBaseCrashlyticsLogger): CrashlyticsLogger

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseAnalytics(): FirebaseAnalytics = Firebase.analytics

        @Provides
        @Singleton
        fun provideFirebaseCrashlytics(): FirebaseCrashlytics = FirebaseCrashlytics.getInstance()
    }
}