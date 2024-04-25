package com.packages.user_profile.restaurant_details

import com.packages.client.restaurant.Item
import com.packages.client.restaurant.Restaurant

data class RestaurantDetailsState(
    val restaurant: Restaurant,
    val menu: List<Item> = emptyList(),
)
