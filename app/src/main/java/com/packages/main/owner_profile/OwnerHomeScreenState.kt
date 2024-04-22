package com.packages.main.owner_profile

import com.packages.client.restaurant.Restaurant
import com.packages.client.restaurant.RestaurantOwner

data class OwnerHomeScreenState(
    val ownerData: RestaurantOwner?,
    var loading: Boolean,
    var error: OwnerHomeScreenError?,
    val restaurants: List<Restaurant>
)

enum class OwnerHomeScreenEvent {
    LOGOUT,
    ADD_RESTAURANT,
    EDIT_RESTAURANT,
    DELETE_RESTAURANT,
    VIEW_RESTAURANT
}

enum class OwnerHomeScreenError {
    OWNER_DATA_NULL,
    RESTAURANT_DATA_NULL,
    RESTAURANT_ADD_FAILED,
    RESTAURANT_EDIT_FAILED,
    RESTAURANT_DELETE_FAILED
}