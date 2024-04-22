package com.packages.main.owner_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.packages.main.repositories.RestaurantOwnerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OwnerViewModel(ownerEmail: String): ViewModel() {
    private val _state = MutableStateFlow(OwnerHomeScreenState(ownerData = null, loading = true, error = null, restaurants = emptyList()))
    val state = _state.asStateFlow()

    init {
        try {
            viewModelScope.launch {
                val ownerData = RestaurantOwnerRepository.getRestaurantOwner(ownerEmail)
                if(ownerData == null) {
                    _state.value.error = OwnerHomeScreenError.OWNER_DATA_NULL
                    _state.value.loading = false
                    return@launch
                }
                val restaurants = RestaurantOwnerRepository.getOwnedRestaurants(ownerData.email)
                _state.value =
                    OwnerHomeScreenState(ownerData, loading = false, error = null, restaurants)
            }
        } catch (e: Exception) {
            _state.value.error = OwnerHomeScreenError.RESTAURANT_DATA_NULL
        }
    }

}