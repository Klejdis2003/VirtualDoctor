package com.packages.data.model.user

import com.packages.data.model.nutrition.NutritionPlan
import com.packages.data.model.nutrition.NutritionType
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String,
    val username: String,
    val age: Int,
    val height: Int,
    val weight: Int,
    val nutritionPlan: NutritionPlan = NutritionPlan(),
    val nutritionType: NutritionType = NutritionType()
)
