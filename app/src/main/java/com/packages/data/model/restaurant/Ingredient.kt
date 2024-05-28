package com.packages.data.model.restaurant

import kotlinx.serialization.Serializable

@Serializable
data class Ingredient(
    val id: Long,
    val name: String,
    val type: String
)
