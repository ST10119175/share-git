package com.example.stepcount

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StepCountDao {
    @Insert
    suspend fun insert(stepCount: StepCount)

    @Query("SELECT * FROM step_counts ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestStepCount(): StepCount?

    @Query("SELECT * FROM step_counts ORDER BY timestamp DESC")
    suspend fun getAllStepCounts(): List<StepCount>
}