package com.umc.halo.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 마지막으로 성공한 소셜 로그인 방식을 저장/조회
 * 재로그인 화면의 '최근 로그인' 표시에 사용
 *
 * 로그아웃하면 토큰은 지우지만(TokenDataStore.clear) 이 값은 남아 있어야 함 TokenDataStore 와 분리함
 *
 */
class LastLoginDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    /** 로그인 성공 시 호출. [provider] 는 SocialProvider 의 이름(KAKAO / GOOGLE) */
    suspend fun saveProvider(provider: String) {
        dataStore.edit { prefs ->
            prefs[LAST_LOGIN_PROVIDER] = provider
        }
    }

    /** 저장된 로그인 방식 (한 번도 로그인한 적 없으면 null) */
    val providerFlow: Flow<String?> = dataStore.data.map { it[LAST_LOGIN_PROVIDER] }

    private companion object {
        val LAST_LOGIN_PROVIDER = stringPreferencesKey("last_login_provider")
    }
}
