package com.packages.data.repositories

import com.packages.data.model.restaurant.Restaurant
import com.packages.data.model.restaurant.RestaurantOwner
import com.packages.main.utils.HttpClient
import io.ktor.util.StringValues

class RestaurantOwnerRepository(private val httpClient: HttpClient = HttpClient()) {
    private val baseUrl = "/restaurant_owners"

    suspend fun get(id: Long): RestaurantOwner? {
        return httpClient.get("$baseUrl/$id")
    }

    suspend fun get(email: String): RestaurantOwner? {
        val params = StringValues.build {
            append("email", email)
        }
        return httpClient.get("$baseUrl/find", params)
    }

    suspend fun getOwnedRestaurants(email: String): List<Restaurant> {
        val params = StringValues.build {
            append("email", email)
        }
        return httpClient.get<List<Restaurant>>("$baseUrl/restaurants", params)!!
    }

    suspend fun exists(email: String?): Boolean {
        if(email == null) return false
        return get(email) != null
    }

    suspend fun createRestaurantOwner(restaurantOwner: RestaurantOwner): RestaurantOwner? {
        return httpClient.post(baseUrl, restaurantOwner)
    }
}