package com.example.stepcount

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StepCount::class], version = 1)
abstract class StepCountDatabase : RoomDatabase() {
    abstract fun stepCountDao(): StepCountDao

    companion object {
        @Volatile
        private var INSTANCE: StepCountDatabase? = null

        fun getDatabase(context: Context): StepCountDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StepCountDatabase::class.java,
                    "step_count_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}