package com.packages.main.repositories

import com.packages.client.restaurant.Item
import com.packages.client.restaurant.Restaurant
import com.packages.main.utils.HttpRequestUtil

class RestaurantRepository {
    companion object{
        private const val BASE = "/restaurants"
        suspend fun getAll() : List<Restaurant> {
            lateinit var restaurants: List<Restaurant>
            val restaurantJson = HttpRequestUtil.getJsonFromRequest(BASE)
            restaurants = if(restaurantJson == null) emptyList()
            else HttpRequestUtil.json.decodeFromString<List<Restaurant>>(restaurantJson)
            return restaurants

        }

        suspend fun get(id: Int): Restaurant? {
            val restaurantJson = HttpRequestUtil.getJsonFromRequest("$BASE/$id")
            return if (restaurantJson == null) return null
            else HttpRequestUtil.json.decodeFromString<Restaurant>(restaurantJson)
        }

        suspend fun getMenu(restaurantId: Long): List<Item> {
            val itemJson = HttpRequestUtil.getJsonFromRequest("$BASE/$restaurantId/menu")
            return HttpRequestUtil.json.decodeFromString<List<Item>>(itemJson!!)
        }
    }
}