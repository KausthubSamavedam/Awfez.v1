package com.example.myapplicationoh.firebase

import android.content.Context
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.example.myapplicationoh.R
import android.app.NotificationManager
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            val notification = NotificationCompat.Builder(
                this,
                "officehub_channel"
            )
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(it.title)
                .setContentText(it.body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()
            val manager =
                getSystemService(Context.NOTIFICATION_SERVICE)
                        as NotificationManager
            manager.notify(1, notification)
        }
    }
}