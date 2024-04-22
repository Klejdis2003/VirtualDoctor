package com.packages.client.user

import com.packages.main.model.user.DietaryRequirements
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String,
    val username: String,
    val age: Int,
    val height: Float,
    val weight: Float,
    val dietaryRequirements: DietaryRequirements
)
