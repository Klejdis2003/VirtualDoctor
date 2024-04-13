package com.packages.client.restaurant

import kotlinx.serialization.Serializable

@Serializable
data class RestaurantOwner(
    val username: String,
    val email: String
)