package com.umc.halo.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import java.util.UUID
import javax.inject.Inject

/**
 * 이 기기를 구분하는 UUID 를 저장/조회
 */
class DeviceUuidDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    /**
     * 저장된 UUID 를 돌려주고, 아직 없으면 새로 만들어 저장한 뒤 돌려줌 (앱 최초 실행 시 1회만 생성됨)
     */
    suspend fun getOrCreate(): String {
        val prefs = dataStore.edit { prefs ->
            if (prefs[DEVICE_UUID] == null) {
                prefs[DEVICE_UUID] = UUID.randomUUID().toString()
            }
        }

        return prefs[DEVICE_UUID]!!
    }

    private companion object {
        val DEVICE_UUID = stringPreferencesKey("device_uuid")
    }
}
