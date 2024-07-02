package com.example.reuniteapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.example.reuniteapp.MainActivity
import com.example.reuniteapp.R
import com.example.reuniteapp.data.AppDatabase
import com.example.reuniteapp.data.UserProfileDao
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView

    private lateinit var userProfileDao: UserProfileDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)

        val database = AppDatabase.getDatabase(this)
        userProfileDao = database.userProfileDao()

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            // Input validation
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this@LoginActivity, "Email and password are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val userProfile = userProfileDao.getUserProfileByEmail(email)

                if (userProfile != null && userProfile.password == password) {
                    saveLoginStatus(true, userProfile.id)
                    saveUserIdToSharedPreferences(userProfile.id)

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        tvRegister.setOnClickListener {
            // Handle click on Register TextView
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    private fun saveLoginStatus(isLoggedIn: Boolean, userId: Int) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        with(sharedPreferences.edit()) {
            putBoolean("isLoggedIn", isLoggedIn)
            putInt("userId", userId)
            apply()
        }
        Log.d("LoginActivity", "Saved login status to SharedPreferences: isLoggedIn=$isLoggedIn, userId=$userId")
    }
    private fun saveUserIdToSharedPreferences(userId: Int) {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putInt("USER_ID", userId)
            apply()
        }
        Log.d("LoginActivity", "Saved user ID to SharedPreferences: $userId")
    }
}