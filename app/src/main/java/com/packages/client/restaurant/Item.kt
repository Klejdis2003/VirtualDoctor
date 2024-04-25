package com.packages.client.restaurant

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: Long,
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Float,
    val calories: Int,
    val sugarContent: Int,
    val fatContent: Int,
    val proteinContent: Int,
    val isVegetarian: Boolean,
    val isVegan: Boolean,
    val itemType: ItemType,
)
enum class ItemType {
    FOOD, DRINK, DESSERT
}