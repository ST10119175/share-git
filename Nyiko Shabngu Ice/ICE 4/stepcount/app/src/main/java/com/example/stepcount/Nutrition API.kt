package com.example.stepcount

import java.util.Date

data class DailyHealthData(
    val date: Date,
    val steps: Int,
    val nutrition: NutritionItem,
    val bmi: Double,
    val nutritionList: List<NutritionItem>, // Renamed to nutritionList
    val healthDataList: List<HealthData> // Renamed to healthDataList
)

data class NutritionItem(
    val name: String = "",
    val fatTotalG: Double = 0.0,
    val fatSaturatedG: Double = 0.0,
    val sodiumMg: Int = 0,
    val potassiumMg: Int = 0,
    val cholesterolMg: Int = 0,
    val carbohydratesTotalG: Double = 0.0,
    val fiberG: Double = 0.0,
    val sugarG: Double = 0.0
)

data class HealthData(
    val bmi: Float,
    val healthStatus: String
)
