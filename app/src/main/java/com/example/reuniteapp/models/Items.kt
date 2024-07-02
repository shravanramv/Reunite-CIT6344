package com.example.reuniteapp.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "items",
    foreignKeys = [ForeignKey(
        entity = UserProfile::class,
        parentColumns = ["username"],
        childColumns = ["foundBy"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Items(
    @PrimaryKey(autoGenerate = true) val itemId: Int = 0,
    val foundBy: String, // Foreign key referencing UserProfile.username
    val found: Boolean = false,
    val itemImage: String,
    val itemTitle: String,
    val location: String = "",
    val date: String = "",
    val time: String = "",
    val itemDescription: String = "",
    val itemCategory: String = "",
    val userEmail: String = "", // Derived from UserProfile.email
    val contactNumber: String = "", // Derived from UserProfile.contactNumber
)