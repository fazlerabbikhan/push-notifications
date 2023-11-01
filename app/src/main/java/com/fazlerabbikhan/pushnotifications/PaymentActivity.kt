package com.fazlerabbikhan.pushnotifications

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.fazlerabbikhan.pushnotifications.databinding.ActivityPaymentBinding
import com.fazlerabbikhan.pushnotifications.service.LocalNotificationService
import com.google.firebase.auth.FirebaseAuth
import android.Manifest
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding
    private lateinit var btnLogout: Button
    private lateinit var auth: FirebaseAuth
    private val PREFS_NAME = "MyAppPrefs"

    private lateinit var serviceIntent: Intent

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
            finishAffinity()

            Toast.makeText(this, "Logged out!", Toast.LENGTH_SHORT).show()
        }

        binding.btnFT.setOnClickListener {

            serviceIntent = Intent(this, LocalNotificationService::class.java)
            serviceIntent.putExtra("title", "Fund Transfer")
            serviceIntent.putExtra("body", "This is Fund Transfer notification")
            serviceIntent.putExtra("image", "https://bangladeshpost.net/webroot/uploads/featureimage/2022-06/62a9e7977bfd0.jpg")

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                if (checkPermission()) {
                    launchService()
                } else {
                    showSnackbar()
                }
            } else {
                askForPermission()
            }
        }

        binding.btnNPSB.setOnClickListener {

            serviceIntent = Intent(this, LocalNotificationService::class.java)
            serviceIntent.putExtra("title", "NPSB")
            serviceIntent.putExtra("body", "This is NPSB notification")
            serviceIntent.putExtra("image", "https://bangladeshpost.net/webroot/uploads/featureimage/2022-06/62a9e7977bfd0.jpg")

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                if (checkPermission()) {
                    launchService()
                } else {
                    showSnackbar()
                }
            } else {
                askForPermission()
            }
        }

        binding.btnEFT.setOnClickListener {

            serviceIntent = Intent(this, LocalNotificationService::class.java)
            serviceIntent.putExtra("title", "EFT")
            serviceIntent.putExtra("body", "This is EFT notification")
            serviceIntent.putExtra("image", "https://bangladeshpost.net/webroot/uploads/featureimage/2022-06/62a9e7977bfd0.jpg")

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                if (checkPermission()) {
                    launchService()
                } else {
                    showSnackbar()
                }
            } else {
                askForPermission()
            }
        }

        binding.btnRTGS.setOnClickListener {

            serviceIntent = Intent(this, LocalNotificationService::class.java)
            serviceIntent.putExtra("title", "RTGS")
            serviceIntent.putExtra("body", "This is RTGS notification")
            serviceIntent.putExtra("image", "https://bangladeshpost.net/webroot/uploads/featureimage/2022-06/62a9e7977bfd0.jpg")

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                if (checkPermission()) {
                    launchService()
                } else {
                    showSnackbar()
                }
            } else {
                askForPermission()
            }
        }
    }

    private fun checkPermission(): Boolean {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return notificationManager.areNotificationsEnabled()
    }

    private fun launchService() {
        startService(serviceIntent)
        intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun askForPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            launchService()
        }
        else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            // The registered ActivityResultCallback gets the result of this request
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        else {
            showSnackbar()
        }
    }

    private fun showSnackbar() {
        Snackbar.make(
            findViewById(R.id.payment_layout),
            "Please turn on notifications!",
            Snackbar.LENGTH_LONG
        ).setAction("Settings") {
            // Responds to click on the action
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val uri: Uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }.show()
    }

    private fun clearAuthState() {
        // Clear the user's authentication state in SharedPreferences
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", false)
        editor.apply()
    }
}