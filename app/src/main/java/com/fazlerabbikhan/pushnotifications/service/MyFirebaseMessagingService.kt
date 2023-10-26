package com.fazlerabbikhan.pushnotifications.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.fazlerabbikhan.pushnotifications.NotificationsActivity
import com.fazlerabbikhan.pushnotifications.R
import com.fazlerabbikhan.pushnotifications.data.Notification
import com.fazlerabbikhan.pushnotifications.repository.NotificationRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val CHANNEL_ID = "my_notification_channel"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val notification = remoteMessage.data
        val title = notification["title"]
        val body = notification["body"]

        Log.d("fcm", "$title, $body")

        val newNotification = Notification(title!!, body!!)
        NotificationRepository.addNotification(newNotification)

        val intent = Intent(this, NotificationsActivity::class.java)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.notification_icon)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationID, builder)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "channelName"
        val channel = NotificationChannel(CHANNEL_ID, channelName, IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)
    }
}



