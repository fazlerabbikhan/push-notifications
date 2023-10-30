package com.fazlerabbikhan.pushnotifications

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fazlerabbikhan.pushnotifications.adapter.NotificationAdapter
import com.fazlerabbikhan.pushnotifications.data.Notification
import com.fazlerabbikhan.pushnotifications.databinding.ActivityNotificationsBinding
import com.fazlerabbikhan.pushnotifications.repository.NotificationRepository
import com.google.firebase.auth.FirebaseAuth

class NotificationsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationsBinding
    private lateinit var btnLogout: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var notificationRecyclerView: RecyclerView
    private val PREFS_NAME = "MyAppPrefs"
    private lateinit var bundle: Bundle

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnLogout = findViewById(R.id.btnLogout)

        auth = FirebaseAuth.getInstance()

        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (!isLoggedIn) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("fromNotification", true)
            startActivity(intent)
            finish()
        }

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = getString(R.string.notifications)

        notificationRecyclerView = findViewById(R.id.notificationRecyclerView)
        notificationRecyclerView.layoutManager = LinearLayoutManager(this)

        if (intent.extras != null) {
            bundle = intent.extras!!

            val title = bundle.getString("title") ?: "Title"
            val body = bundle.getString("body") ?: "Body"

            Log.d("IntentExtras", "$title, $body")

            val newNotification = Notification(title, body)
            NotificationRepository.addNotification(newNotification)
        }

        val notifications = NotificationRepository.getNotifications()
        val adapter = NotificationAdapter(notifications)
        notificationRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        Log.d("notifications", "$notifications")

        btnLogout.setOnClickListener {
            // Sign out the user when the logout button is clicked
            auth.signOut()

            clearAuthState()

            // Redirect the user back to the login screen
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()

            Toast.makeText(this, "Logged out!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearAuthState() {
        // Clear the user's authentication state in SharedPreferences
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", false)
        editor.apply()
    }
}