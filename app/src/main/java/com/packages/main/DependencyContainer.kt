package com.packages.main

import com.packages.data.repositories.ItemRepository
import com.packages.data.repositories.NutritionRepository
import com.packages.data.repositories.RestaurantOwnerRepository
import com.packages.data.repositories.RestaurantRepository
import com.packages.data.repositories.UserRepository
import com.packages.main.utils.HttpClient

class DependencyContainer {
    private val httpClient = HttpClient()
    val userRepository by lazy { UserRepository(httpClient) }
    val itemRepository by lazy { ItemRepository(httpClient) }
    val restaurantRepository by lazy { RestaurantRepository(httpClient) }
    val restaurantOwnerRepository by lazy { RestaurantOwnerRepository(httpClient) }
    val nutritionRepository by lazy { NutritionRepository(httpClient) }
}