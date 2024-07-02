package com.example.reuniteapp.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.reuniteapp.data.AppDatabase
import com.example.reuniteapp.models.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val userProfileDao = AppDatabase.getDatabase(application).userProfileDao()

    fun getUserProfileById(userId: Int, onSuccess: (UserProfile) -> Unit, onError: (Exception) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userProfile = userProfileDao.getUserProfileById(userId)
                if (userProfile != null) {
                    onSuccess(userProfile)
                } else {
                    throw Exception("User profile not found")
                }
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun updateUserProfile(userProfile: UserProfile, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userProfileDao.updateUserProfile(userProfile)
                onSuccess()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}