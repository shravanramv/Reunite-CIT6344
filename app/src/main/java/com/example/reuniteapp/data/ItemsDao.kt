package com.example.reuniteapp.data

import androidx.room.*
import com.example.reuniteapp.models.Items

@Dao
interface ItemsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Items)

    @Update
    suspend fun update(item: Items)

    @Query("SELECT * FROM items WHERE foundBy = :username")
    suspend fun getItemsByUser(username: String): List<Items>

    @Query("SELECT * FROM items WHERE itemId = :itemId")
    suspend fun getItemById(itemId: Int): Items?

    @Query("SELECT * FROM items WHERE found = :found")
    suspend fun getItemsByFoundStatus(found: Boolean): List<Items>
}