package com.example.reuniteapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.reuniteapp.models.UserProfile

@Database(entities = [UserProfile::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "reunite_app_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }


        // Define your migration from version 1 to version 2
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Migration strategy: For example, adding a new column
                db.execSQL("ALTER TABLE user_profiles ADD COLUMN profilePic TEXT DEFAULT '' NOT NULL")
            }
        }
    }
}