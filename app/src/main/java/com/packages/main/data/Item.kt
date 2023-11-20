package com.packages.main.data

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
}