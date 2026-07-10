package com.umc.halo.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * DataStore(Preferences) 의존성을 Hilt 로 제공하는 모듈
 *
 * DataStore 는 앱 전역에서 하나의 인스턴스만 존재해야 하므로
 * preferencesDataStore 델리게이트를 파일 최상단(top-level)에 두고 그것을 Hilt 로 감싸 제공
 *
 * 사용범 : DataStore<Preferences> 를 @Inject 로 받아 토큰/guestUuid 등 간단한 키-값 저장에 활용 가능
 *   예) class TokenLocalDataSource @Inject constructor(dataStore: DataStore<Preferences>)
 */
private const val PREFERENCES_NAME = "halo_preferences"

// Context 확장 프로퍼티 — 프로세스당 단 하나의 DataStore 인스턴스를 보장한다.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = PREFERENCES_NAME
)

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.dataStore
}
