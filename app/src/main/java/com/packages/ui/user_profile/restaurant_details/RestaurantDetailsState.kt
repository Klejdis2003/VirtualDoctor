package com.packages.ui.user_profile.restaurant_details

import com.packages.data.model.restaurant.Item
import com.packages.data.model.restaurant.Restaurant

data class RestaurantDetailsState(
    val restaurant: Restaurant,
    val menu: List<Item> = emptyList(),
)
