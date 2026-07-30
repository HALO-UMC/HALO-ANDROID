package com.umc.halo

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

/**
 * Hilt DI 컨테이너의 뿌리역할을 하는 Application 클래스
 *
 * @HiltAndroidApp 를 붙이면 Hilt 가 앱 전역에서 의존성을 주입할 수 있는 코드를 생성
 * 이 클래스는 AndroidManifest 의 <application android:name> 에 등록되어야 실제로 동작함
 */
@HiltAndroidApp
class HaloApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // 카카오 SDK 초기화 (앱 실행 시 1회) — 네이티브 앱 키는 local.properties → BuildConfig 로 주입
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)

        // 푸시 알림 채널 설정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "default",
                "기본 알림",
                NotificationManager.IMPORTANCE_HIGH
            )

            val notificationManager =
                getSystemService(NotificationManager::class.java)

            notificationManager.createNotificationChannel(channel)
        }
    }
}
