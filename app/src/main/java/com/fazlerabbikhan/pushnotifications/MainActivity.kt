package com.fazlerabbikhan.pushnotifications

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.fazlerabbikhan.pushnotifications.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var btnLogout: Button
    private lateinit var auth: FirebaseAuth
    private val PREFS_NAME = "MyAppPrefs"

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnLogout = findViewById(R.id.btnLogout)

        auth = FirebaseAuth.getInstance()

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = getString(R.string.dashboard)

        btnLogout.setOnClickListener {
            // Sign out the user when the logout button is clicked
            auth.signOut()

            clearAuthState()

            // Redirect the user back to the login screen
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finishAffinity()

            Toast.makeText(this, "Logged out!", Toast.LENGTH_SHORT).show()
        }

        binding.btnNotifications.setOnClickListener {
            val intent = Intent(this, NotificationsActivity::class.java)
            startActivity(intent)
        }

        binding.btnPayment.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            startActivity(intent)
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("exception", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get the new FCM registration token
            val token = task.result

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d("token", msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
    }

    private fun clearAuthState() {
        // Clear the user's authentication state in SharedPreferences
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", false)
        editor.apply()
    }
}



