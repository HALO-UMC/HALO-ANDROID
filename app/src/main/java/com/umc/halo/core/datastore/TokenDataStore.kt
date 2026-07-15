package com.umc.halo.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * access/refresh 토큰을 DataStore에 저장/조회
 * 자동 로그인(앱 실행 시 refreshToken 으로 재발급) Authorization 헤더 첨부에서 재사용함
 * DataStore<Preferences> 는 DataStoreModule 이 제공하므로 @Inject 로 바로 주입받음
 */
class TokenDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    // 로그인과 재발급 성공 시 두 토큰을 함께 저장
    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = accessToken
            prefs[REFRESH_TOKEN] = refreshToken
        }
    }

    // 저장된 토큰 (없으면 null)
    val accessTokenFlow: Flow<String?> = dataStore.data.map { it[ACCESS_TOKEN] }
    val refreshTokenFlow: Flow<String?> = dataStore.data.map { it[REFRESH_TOKEN] }

    // 로그아웃과 재발급 실패 시 토큰 제거
    suspend fun clear() {
        dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN)
            prefs.remove(REFRESH_TOKEN)
        }
    }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }
}
