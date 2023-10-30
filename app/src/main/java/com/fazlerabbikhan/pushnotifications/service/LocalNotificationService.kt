package com.fazlerabbikhan.pushnotifications.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.fazlerabbikhan.pushnotifications.NotificationsActivity
import com.fazlerabbikhan.pushnotifications.R
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.random.Random

class LocalNotificationService : Service() {

    private val CHANNEL_ID = "local_notifications"
    private val CHANNEL_NAME = "local_channel"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent != null) {
            val title = intent.getStringExtra("title") ?: "Title"
            val body = intent.getStringExtra("body") ?: "Body"
            val image = intent.getStringExtra("image") ?: ""

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val imageBitmap = Picasso.get().load(image).get()
                    showNotification(title, body, imageBitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                    val placeholderBitmap = BitmapFactory.decodeResource(resources, R.drawable.bank_asia)
                    showNotification(title, body, placeholderBitmap)
                }
            }
        }

        return START_NOT_STICKY
    }

    private fun showNotification(title: String, body: String, bitmap: Bitmap) {
        val intent = Intent(this, NotificationsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        intent.putExtra("title", title)
        intent.putExtra("body", body)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 1, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.notification_icon)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setLargeIcon(bitmap)
            .setStyle(
                NotificationCompat.BigPictureStyle().bigLargeIcon(null).bigPicture(bitmap)
            )
            .build()

        notificationManager.notify(notificationID, builder)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}