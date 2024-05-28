package com.packages.data.model.nutrition

import kotlinx.serialization.Serializable

@Serializable
data class NutritionPlan(
    val id: Long = 0,
    val name: String = "No plan",
    val description: String = "",
    val nutritionValues: NutritionValues = NutritionValues()
)