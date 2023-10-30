package com.fazlerabbikhan.pushnotifications

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.fazlerabbikhan.pushnotifications.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private val PREFS_NAME = "MyAppPrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        // Check if the activity was started from a notification
        val fromNotification = intent.getBooleanExtra("fromNotification", false)

        if (isLoggedIn) {
            if (fromNotification) {
                val intent = Intent(this, NotificationsActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.etUsername.setText("admin@era.com")
        binding.etPassword.setText("admin@123")

        binding.btnLogin.setOnClickListener {
            loginUser(fromNotification)
        }

        binding.tvSignUp.setOnClickListener {
            // You can add code to navigate to the signup screen here
        }
    }

    private fun loginUser(fromNotification: Boolean) {
        val email = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                        // Handle navigation based on the fromNotification flag
                        if (fromNotification) {
                            val intent = Intent(this, NotificationsActivity::class.java)
                            startActivity(intent)
                        } else {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                        finish()

                        storeAuthState()
                    } else {
                        // Login failed, display an error message
                        // You can customize error handling as needed
                        Toast.makeText(this, "Enter correct credentials!", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Please enter email & password", Toast.LENGTH_SHORT).show()
        }
    }

    private fun storeAuthState(){
        // Store user's authentication state in SharedPreferences after a successful login
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", true)
        editor.apply()
    }
}