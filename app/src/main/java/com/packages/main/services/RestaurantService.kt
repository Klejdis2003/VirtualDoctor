package com.packages.main.services

import com.packages.client.restaurant.Restaurant
import com.packages.main.utils.HttpRequestUtil

class RestaurantService {
    companion object{
        suspend fun getAll() : List<Restaurant> {
            val base = "/restaurants"
            val restaurants_json = HttpRequestUtil.getJsonFromRequest(base)
            return if(restaurants_json == null) emptyList()
            else HttpRequestUtil.json.decodeFromString<List<Restaurant>>(restaurants_json)
        }
    }
}