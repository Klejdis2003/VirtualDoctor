package com.packages.data.model.restaurant

import kotlinx.serialization.Serializable

@Serializable
data class Restaurant(
    val id: Long = 0,
    val name: String,
    val streetAddress: String,
    val city: String,
    val postcode: String,
    val country: String,
    val telephone: String,
    val email: String,
    val website: String,
    val owner: RestaurantOwner? = null,
)

