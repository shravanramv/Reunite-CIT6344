package com.example.reuniteapp

import com.example.reuniteapp.ui.LoginActivity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.reuniteapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        val userId = sharedPreferences.getInt("USER_ID", -1)

        // Log the values of isLoggedIn and userId
        Log.d("MainActivity", "isLoggedIn: $isLoggedIn, USER_ID: $userId")

        if (!isLoggedIn) {
            navigateToLogin()
            return
        }

        // Continue with normal activity setup
        setupActivity()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupActivity() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            navController.navigate(R.id.action_navigation_home_to_addReportFragment)
        }
    }

    private fun createNewReport() {
        // Your logic to handle creating a new report
        // For example, navigate to a new activity or show a dialog
    }

    private fun logout() {
        // Clear the login state in SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", false)
        editor.apply()

        // Navigate to the login page
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
