package com.fazlerabbikhan.pushnotifications.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.fazlerabbikhan.pushnotifications.NotificationsActivity
import com.fazlerabbikhan.pushnotifications.R
import com.fazlerabbikhan.pushnotifications.data.Notification
import com.fazlerabbikhan.pushnotifications.data.TokenRequest
import com.fazlerabbikhan.pushnotifications.data.TokenResponse
import com.fazlerabbikhan.pushnotifications.repository.NotificationRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val CHANNEL_ID = "fcm_notifications"
    private val CHANNEL_NAME = "fcm_channel"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val notification = remoteMessage.data
        val title = notification["title"] ?: "Title"
        val body = notification["body"] ?: "Body"
        val image = notification["image"] ?: ""

        Log.d("fcm", "$title, $body, $image")

        val newNotification = Notification(title, body)
        NotificationRepository.addNotification(newNotification)

        try {
            val imageBitmap = Picasso.get().load(image).get()
            showNotification(title, body, imageBitmap)
        } catch (e: Exception) {
            e.printStackTrace()
            val placeholderBitmap = BitmapFactory.decodeResource(resources, R.drawable.bank_asia)
            showNotification(title, body, placeholderBitmap)
        }
    }

    private fun showNotification(title: String, body: String, bitmap: Bitmap) {
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
            .setLargeIcon(bitmap)
            .setStyle(NotificationCompat.BigPictureStyle().bigLargeIcon(null).bigPicture(bitmap))
            .build()

        notificationManager.notify(notificationID, builder)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        // Send the new token to your server
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {
        // Create a Retrofit instance with your server's base URL
        val retrofit = Retrofit.Builder()
            .baseUrl("https://httpbin.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create an instance of the service
        val apiService = retrofit.create(ApiService::class.java)

        // Call the updateToken method to send the new token to your server
        val call = apiService.updateToken(TokenRequest(token))

        // Use enqueue for asynchronous execution
        call.enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                if (response.isSuccessful) {
                    // Token update was successful
                    val tokenResponse = response.body()
                    val token = tokenResponse?.json?.token
                    if (token != null) {
                        Log.d("TokenUpdate", "Token updated successfully. New token: $token")
                    } else {
                        Log.e("TokenUpdate", "Token update response is empty.")
                    }
                } else {
                    // Handle the error
                    Log.e("TokenUpdate", "Token update failed with response code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                // Handle the network request failure
                Log.e("TokenUpdate", "Network request failed with error: ${t.message}")
            }
        })
    }
}



