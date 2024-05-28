package com.packages.data.model.nutrition

import kotlinx.serialization.Serializable

@Serializable
data class NutritionValues(
    val id : Long = 0,
    val calories: Int = Int.MAX_VALUE,
    val protein: Int = Int.MAX_VALUE,
    val carbohydrates: Int = Int.MAX_VALUE,
    val fat: Int = Int.MAX_VALUE
)
