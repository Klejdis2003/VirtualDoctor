package com.packages.ui.owner_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.packages.data.model.restaurant.RestaurantOwner
import com.packages.data.repositories.RestaurantOwnerRepository
import com.packages.data.repositories.RestaurantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OwnerViewModel(
    private val ownerEmail: String,
    private val restaurantOwnerRepository: RestaurantOwnerRepository,
    val restaurantRepository: RestaurantRepository
): ViewModel() {
    private val _state = MutableStateFlow(OwnerHomeScreenState())
    val state = _state.asStateFlow()
    var owner: RestaurantOwner? = null
    init {
        updateState()
    }

   fun updateState(){
        try {
            viewModelScope.launch {
                val ownerData = restaurantOwnerRepository.get(ownerEmail)
                if(ownerData == null) {
                    _state.value.error = OwnerHomeScreenError.OWNER_DATA_NULL
                    _state.value.loading = false
                    return@launch
                }
                val restaurants = restaurantOwnerRepository.getOwnedRestaurants(ownerData.email)
                _state.value =
                    OwnerHomeScreenState(ownerData, loading = false, error = null, restaurants)
                owner = restaurantOwnerRepository.get(ownerEmail)
            }
        } catch (e: Exception) {
            _state.value.error = OwnerHomeScreenError.RESTAURANT_DATA_NULL
        }
    }

}