package com.packages.data.model.nutrition

import kotlinx.serialization.Serializable

@Serializable
data class NutritionType(
    val id: Long = 0,
    val name: String = "Omnivore",
)
