package com.packages.main.backend.model

import kotlinx.serialization.Serializable

@Serializable
data class RestaurantOwner(
    val id: Long,
    val username: String,
    val email: String
)