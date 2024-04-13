package com.packages.client.restaurant

data class Item(
    val name: String,
    var price: Float,
    val description: String,
    val calories: Int,
    val ingredients: List<String>,
    val type: ItemType
) : Comparable<Item>
{
    override fun compareTo(other: Item): Int {
        return name.compareTo(other.name, ignoreCase = true)
    }

    override fun equals(other: Any?): Boolean {
        return name == (other as Item).name
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + calories
        result = 31 * result + ingredients.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }
}