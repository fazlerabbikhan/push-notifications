package com.fazlerabbikhan.pushnotifications

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.fazlerabbikhan.pushnotifications.databinding.ActivityPaymentBinding
import com.fazlerabbikhan.pushnotifications.service.LocalNotificationService
import com.google.firebase.auth.FirebaseAuth

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding
    private lateinit var btnLogout: Button
    private lateinit var auth: FirebaseAuth
    private val PREFS_NAME = "MyAppPrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnLogout = findViewById(R.id.btnLogout)

        auth = FirebaseAuth.getInstance()

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = getString(R.string.make_payment)

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

        binding.btnFT.setOnClickListener {

            val serviceIntent = Intent(this, LocalNotificationService::class.java)

            serviceIntent.putExtra("title", "Fund Transfer")
            serviceIntent.putExtra("body", "This is Fund Transfer notification")
            serviceIntent.putExtra("image", "https://bangladeshpost.net/webroot/uploads/featureimage/2022-06/62a9e7977bfd0.jpg")

            startService(serviceIntent)

            intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

        binding.btnNPSB.setOnClickListener {

            val serviceIntent = Intent(this, LocalNotificationService::class.java)

            serviceIntent.putExtra("title", "NPSB")
            serviceIntent.putExtra("body", "This is NPSB notification")
            serviceIntent.putExtra("image", "https://bangladeshpost.net/webroot/uploads/featureimage/2022-06/62a9e7977bfd0.jpg")

            startService(serviceIntent)

            intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

        binding.btnEFT.setOnClickListener {

            val serviceIntent = Intent(this, LocalNotificationService::class.java)

            serviceIntent.putExtra("title", "EFT")
            serviceIntent.putExtra("body", "This is EFT notification")
            serviceIntent.putExtra("image", "https://bangladeshpost.net/webroot/uploads/featureimage/2022-06/62a9e7977bfd0.jpg")

            startService(serviceIntent)

            intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

        binding.btnRTGS.setOnClickListener {

            val serviceIntent = Intent(this, LocalNotificationService::class.java)

            serviceIntent.putExtra("title", "RTGS")
            serviceIntent.putExtra("body", "This is RTGS notification")
            serviceIntent.putExtra("image", "https://bangladeshpost.net/webroot/uploads/featureimage/2022-06/62a9e7977bfd0.jpg")

            startService(serviceIntent)

            intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
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