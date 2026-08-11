package com.umc.halo.notification

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.umc.halo.R
import com.umc.halo.core.datastore.DeviceUuidDataStore
import com.umc.halo.domain.repository.notification.NotificationRepository
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HaloFirebaseMessagingService: FirebaseMessagingService() {
    @Inject lateinit var deviceUuidDataStore: DeviceUuidDataStore
    @Inject lateinit var notificationRepository: NotificationRepository

    @Deprecated("Deprecated in Java")
    //FCM 토큰 변경 시
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        // 등록 실패해도 앱이 죽지 않게 에외처리 — 이 코루틴은 화면과 무관한 백그라운드에서
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                val uuid = deviceUuidDataStore.getOrCreate()

                notificationRepository.addMembers(token, uuid)
            }
        }
    }

    // 앱 실행 중 알림 도착
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d("FCM", message.data.toString())

        showNotification(
            title = message.notification?.title ?: "HALO",
            body = message.notification?.body ?: ""
        )
    }

    private fun showNotification(
        title: String?,
        body: String?
    ) {
        val notification =
            NotificationCompat.Builder(this, "default")
                // 푸시 알림 아이콘에서의 HALO 손 모양
                .setSmallIcon(R.drawable.ic_notification)
                // 아이콘 배경색
                .setColor(ContextCompat.getColor(this, R.color.notification_accent))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .build()

        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        NotificationManagerCompat
            .from(this)
            .notify(1, notification)
    }
}

