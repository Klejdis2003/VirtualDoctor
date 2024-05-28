package com.packages.ui.user_profile

import com.packages.data.model.restaurant.Restaurant
import com.packages.data.model.user.Stats
import com.packages.data.model.user.User

data class UserHomeScreenState(
    val user: User? = null,
    val restaurants: List<Restaurant> = emptyList(),
    val stats: Stats? = Stats(),
    val clickedRestaurant: Restaurant? = null,
    val loading: Boolean = true,
    val updatedStatsState : UpdateStatsState = UpdateStatsState.NotStarted
)

sealed class UpdateStatsState {
    data object NotStarted : UpdateStatsState()
    data object Success : UpdateStatsState()
    data object Error : UpdateStatsState()
}
