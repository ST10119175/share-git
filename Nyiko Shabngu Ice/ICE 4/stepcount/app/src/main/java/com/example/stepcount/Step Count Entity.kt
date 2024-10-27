package com.example.stepcount

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "step_counts")
data class StepCount(
    @PrimaryKey(autoGenerate = true)

    val id: Int = 0,
    val count: Int,
    val timestamp: Long = System.currentTimeMillis()
)