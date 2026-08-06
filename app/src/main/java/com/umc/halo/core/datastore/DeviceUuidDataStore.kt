package com.umc.halo.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import java.util.UUID
import javax.inject.Inject

/**
 * 이 기기를 구분하는 UUID 를 저장/조회
 * FCM 토큰을 서버에 등록할 때 "어느 기기의 토큰인지" 알려주는 용도로 사용
 *
 * 한 번 만들어지면 앱을 지울 때까지 값이 바뀌지 않아야 함
 * FCM 토큰은 재설치/캐시 삭제 등으로 바뀌지만 이 값은 그대로 남아 서버가 같은 기기임을 알 수 있음
 *
 * 로그아웃해도 지우지 않음 (TokenDataStore.clear 는 토큰 키 2개만 지우므로 자동으로 안전)
 * 기기 자체를 가리키는 값이라 계정이 바뀌어도 유지되는 게 맞음
 */
class DeviceUuidDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    /**
     * 저장된 UUID 를 돌려주고, 아직 없으면 새로 만들어 저장한 뒤 돌려줌 (앱 최초 실행 시 1회만 생성됨)
     *
     * '읽어보고 없으면 쓴다' 를 반드시 edit 블록 '안에서' 해야 함
     * edit 은 트랜잭션이라 동시에 여러 곳에서 불러도 한 번에 하나씩만 실행됨
     * 밖에서 읽고 → 없으면 쓰기로 나누면 두 곳이 동시에 "없다" 를 보고 서로 다른 UUID 를 만들어 덮어쓸 수 있음
     */
    suspend fun getOrCreate(): String {
        val prefs = dataStore.edit { prefs ->
            if (prefs[DEVICE_UUID] == null) {
                prefs[DEVICE_UUID] = UUID.randomUUID().toString()
            }
        }
        // edit 은 수정이 끝난 뒤의 값을 돌려줌 바로 위에서 채워 넣었으므로 null 일 수 없음
        return prefs[DEVICE_UUID]!!
    }

    private companion object {
        val DEVICE_UUID = stringPreferencesKey("device_uuid")
    }
}
