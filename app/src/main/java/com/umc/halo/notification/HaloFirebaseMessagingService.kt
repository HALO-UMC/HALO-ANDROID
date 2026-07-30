package com.umc.halo.notification

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.umc.halo.R

class HaloFirebaseMessagingService : FirebaseMessagingService() {

    @Deprecated("Deprecated in Java")
    //FCM 토큰 변경 시
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d("FCM", "New Token = $token")

        // TODO 서버에 전송
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
                .setSmallIcon(R.drawable.ic_launcher_foreground)
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

