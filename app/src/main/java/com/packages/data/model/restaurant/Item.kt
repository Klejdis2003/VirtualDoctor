package com.packages.data.model.restaurant

import com.packages.data.model.nutrition.NutritionValues
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: Long,
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Float,
    val type: ItemType,
    val nutritionValues: NutritionValues,
    val ingredients : List<Ingredient>,
    val restaurant: Restaurant? = null
)
enum class ItemType {
    FOOD, DRINK, DESSERT
}