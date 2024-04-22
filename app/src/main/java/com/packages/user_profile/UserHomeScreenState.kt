package com.packages.user_profile

import com.packages.client.restaurant.Restaurant
import com.packages.client.user.User

data class UserHomeScreenState(
    val user: User?,
    val restaurants: List<Restaurant>,
    val loading: Boolean
)