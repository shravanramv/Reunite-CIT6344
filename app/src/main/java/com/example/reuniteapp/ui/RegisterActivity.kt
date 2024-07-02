package com.example.reuniteapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.reuniteapp.MainActivity
import com.example.reuniteapp.R
import com.example.reuniteapp.data.AppDatabase
import com.example.reuniteapp.data.UserProfileDao
import com.example.reuniteapp.models.UserProfile
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etContactNumber: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button

    private lateinit var userProfileDao: UserProfileDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etContactNumber = findViewById(R.id.etContactNumber)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnRegister = findViewById(R.id.btnRegister)

        val database = AppDatabase.getDatabase(this)
        userProfileDao = database.userProfileDao()

        btnRegister.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val contactNumber = etContactNumber.text.toString()
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (name.isEmpty() || email.isEmpty() || contactNumber.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userProfile = UserProfile(
                name = name,
                email = email,
                contactNumber = contactNumber,
                username = username,
                password = password,
                profilePic = ""
            )

            lifecycleScope.launch {
                try {
                    val userIdLong = userProfileDao.insert(userProfile)
                    val userId = userIdLong.toInt()

                    // Save user ID to SharedPreferences
                    saveUserIdToSharedPreferences(userId)

                    Toast.makeText(this@RegisterActivity, "Registration successful", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this@RegisterActivity, "Failed to register user", Toast.LENGTH_SHORT).show()
                    Log.e("RegisterActivity", "Error registering user", e)
                }
            }
        }
    }

    private fun saveUserIdToSharedPreferences(userId: Int) {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putInt("USER_ID", userId)
            apply()
        }
        Log.d("RegisterActivity", "Saved user ID to SharedPreferences: $userId")
    }
}