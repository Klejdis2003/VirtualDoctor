package com.packages.data.model.restaurant

import kotlinx.serialization.Serializable

@Serializable
data class RestaurantOwner(
    val id: Int = 0,
    val username: String,
    val email: String
)