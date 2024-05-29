package com.packages.data.repositories

import com.packages.data.model.restaurant.Item
import com.packages.data.model.restaurant.Restaurant
import com.packages.main.utils.HttpClient

class RestaurantRepository(private val httpClient: HttpClient = HttpClient()) {
    private val base = "/restaurants"

    suspend fun getAll(): List<Restaurant> {
        return httpClient.get(base)!!
    }

    suspend fun get(id: Int): Restaurant? {
        return httpClient.get("$base/$id")!!
    }

    suspend fun getMenu(restaurantId: Long): List<Item> {
        return httpClient.get("$base/$restaurantId/menu")!!
    }

    suspend fun addItemToMenu(restaurantId: Long, item: Item): List<Item> {
        return httpClient.postWithDifferentResponseType<Item, List<Item>>("$base/$restaurantId/menu", item)!!
    }
    suspend fun create(restaurant: Restaurant): Restaurant? {
        return httpClient.post(base, restaurant)
    }

    suspend fun update(restaurant: Restaurant): Restaurant? {
        return httpClient.put("$base/${restaurant.id}", restaurant)
    }

    suspend fun delete(id: Int): Unit {
        httpClient.delete<Restaurant>("$base/$id")
    }

}