package com.packages.ui.owner_profile

import com.packages.data.model.restaurant.Restaurant
import com.packages.data.model.restaurant.RestaurantOwner

data class OwnerHomeScreenState(
    val ownerData: RestaurantOwner? = null,
    var loading: Boolean = true,
    var error: OwnerHomeScreenError? = null,
    val restaurants: List<Restaurant> = emptyList(),
    val clickedRestaurant: Restaurant? = null,
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