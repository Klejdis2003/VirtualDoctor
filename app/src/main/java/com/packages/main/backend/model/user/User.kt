package com.packages.main.backend.model.user

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username: String,
    val age: Int,
    val height: Float,
    val weight: Float,
    val calorieLimit: Int,
    val maxSugarContent: Int,
    val maxFatContent: Int,
    val maxProteinContent: Int,
    val isVegetarian: Boolean = false,
    val isVegan: Boolean = false,
){
    override fun toString(): String {
        return "User(username='$username', calorieLimit=$calorieLimit, maxSugarContent=$maxSugarContent, maxFatContent=$maxFatContent, maxProteinContent=$maxProteinContent, isVegetarian=$isVegetarian, isVegan=$isVegan)"
    }
}
