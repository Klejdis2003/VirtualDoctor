package com.packages.main.model.user

import kotlinx.serialization.Serializable

@Serializable
data class DietaryRequirements (
    val id: Long = 0,
    var calorieLimit: Int = Int.MAX_VALUE,
    var maxSugarContent: Int = Int.MAX_VALUE,
    var maxFatContent: Int = Int.MAX_VALUE,
    var maxProteinContent: Int = Int.MAX_VALUE,
    var isVegetarian: Boolean = false,
    var isVegan: Boolean = false
)

