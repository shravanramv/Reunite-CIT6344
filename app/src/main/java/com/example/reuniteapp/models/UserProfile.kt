package com.example.reuniteapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "user_profiles",
    indices = [Index(value = ["username"], unique = true)]
)
data class UserProfile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String = "",
    var contactNumber: String = "",
    val username: String = "", // Added default value to ensure no-arg constructor
    var password: String = "",
    var profileImageUri: String = "",
    var email: String = "",
    val profilePic: String = ""
)