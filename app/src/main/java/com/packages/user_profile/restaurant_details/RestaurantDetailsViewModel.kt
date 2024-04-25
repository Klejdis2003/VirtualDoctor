package com.packages.user_profile.restaurant_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.packages.client.restaurant.Item
import com.packages.client.restaurant.Restaurant
import com.packages.main.repositories.RestaurantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RestaurantDetailsViewModel(private val restaurant: Restaurant, private val onAddButtonPressed: (item: Item) -> Unit = {}) : ViewModel() {
    private val _state = MutableStateFlow(RestaurantDetailsState(restaurant = restaurant))
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            // Fetch menu items for the restaurant
            val menu = RestaurantRepository.getMenu(restaurant.id)
            _state.value = _state.value.copy(menu = menu)
        }
    }

    fun addItemToDailyIntake(item: Item) {
        onAddButtonPressed(item)
    }
}



class RestaurantDetailsViewModelFactory(private val restaurant: Restaurant, private val onAddButtonPressed: (item: Item) -> Unit) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RestaurantDetailsViewModel::class.java)) {
            return RestaurantDetailsViewModel(restaurant, onAddButtonPressed) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}