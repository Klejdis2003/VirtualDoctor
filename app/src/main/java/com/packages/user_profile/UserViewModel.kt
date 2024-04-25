package com.packages.user_profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.packages.client.restaurant.Item
import com.packages.client.restaurant.Restaurant
import com.packages.main.repositories.RestaurantRepository
import com.packages.main.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HomeViewModel(userEmail: String?): ViewModel()  {

    private var _state = MutableStateFlow(UserHomeScreenState())
    var state = _state.asStateFlow()
    val context = this

    init {
        if (userEmail != null) {
            getUser(userEmail)
            getRestaurants()
            getStats()
            _state.value = _state.value.copy(loading = false)
        }

    }

    private fun getUser(userEmail: String){
        runBlocking{
            launch {
                try {
                    val user = UserRepository.getByEmail(userEmail)
                    _state.value = _state.value.copy(user = user)
                } catch (e: Exception) {
                    Log.w("HomeViewModel", "Error while fetching data: ${e.message}")
                }
            }
        }
    }

    private fun getRestaurants() {
        viewModelScope.launch {
            try {
                val restaurants = RestaurantRepository.getAll()
                _state.value = _state.value.copy(restaurants = restaurants)
            }
            catch (e: Exception){
                Log.w("HomeViewModel", "Error while fetching data: ${e.message}")
            }
        }
    }

    private fun getStats() {
        viewModelScope.launch {
            try {
                val stats = state.value.user?.let { UserRepository.getDailyStats(it) }
                _state.value = _state.value.copy(stats = stats)
            }
            catch (e: Exception){
                Log.w("HomeViewModel", "Error while fetching data: ${e.message}")
            }
        }
    }

    fun onRestaurantClicked(restaurant: Restaurant){
        _state.value = _state.value.copy(clickedRestaurant = restaurant)
    }

    fun onAddButtonPressed(item: Item){
       viewModelScope.launch {
           try {
               val updatedStats = UserRepository.addUserItem(state.value.user!!, item)
               if (updatedStats != null) {
                   _state.value = _state.value.copy(
                       stats = updatedStats,
                       updatedStatsState = UpdateStatsState.Success
                   )
               } else
                   _state.value = _state.value.copy(updatedStatsState = UpdateStatsState.Error)
           } catch (e: Exception) {
               _state.value = _state.value.copy(updatedStatsState = UpdateStatsState.Error)
           }
       }
    }

    fun onStatsToastDismissed(){
        _state.value = _state.value.copy(updatedStatsState = UpdateStatsState.NotStarted)
    }
}
