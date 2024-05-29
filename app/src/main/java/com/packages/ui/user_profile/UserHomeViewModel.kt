package com.packages.ui.user_profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.packages.data.model.restaurant.Item
import com.packages.data.model.restaurant.Restaurant
import com.packages.data.repositories.ItemRepository
import com.packages.data.repositories.RestaurantRepository
import com.packages.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HomeViewModel(
    userEmail: String?,
    private val userRepository: UserRepository,
    val restaurantRepository: RestaurantRepository,
    val itemRepository: ItemRepository,
    val onNullUser : () -> Unit = {}
    ): ViewModel()  {

    private var _state = MutableStateFlow(UserHomeScreenState())
    var state = _state.asStateFlow()

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
                    val user = userRepository.get(userEmail)
                    _state.value = _state.value.copy(user = user)
                    if (user == null) onNullUser()
                } catch (e: Exception) {
                    Log.w("HomeViewModel", "Error while fetching data: ${e.message}")
                }
            }
        }
    }

    private fun getRestaurants() {
        viewModelScope.launch {
            try {
                val restaurants = restaurantRepository.getAll()
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
                val stats = state.value.user?.let { userRepository.getDailyStats(it) }
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
               val updatedStats = userRepository.addUserItem(state.value.user!!, item)
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
