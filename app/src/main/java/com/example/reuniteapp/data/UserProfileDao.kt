package com.example.reuniteapp.data

import androidx.room.*
import com.example.reuniteapp.models.UserProfile

@Dao
interface UserProfileDao {
    @Insert
    suspend fun insert(userProfile: UserProfile) : Long

    @Query("SELECT * FROM user_profiles WHERE email = :email")
    suspend fun getUserProfileByEmail(email: String): UserProfile?

    @Query("SELECT * FROM user_profiles WHERE id = :userId")
    suspend fun getUserProfileById(userId: Int): UserProfile?

    @Update
    suspend fun updateUserProfile(userProfile: UserProfile)
}