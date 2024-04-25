package com.packages.client.user

import kotlinx.serialization.Serializable

@Serializable
data class Stats(
    val id: Long = 0,
    val calories: Int = 0,
    val carbohydrates: Int = 0,
    val protein : Int = 0,
    val fat: Int = 0,
)
