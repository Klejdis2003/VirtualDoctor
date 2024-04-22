package com.packages.user_profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.packages.main.repositories.RestaurantRepository
import com.packages.main.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(userEmail: String?): ViewModel()  {

    private var _state = MutableStateFlow(UserHomeScreenState(user = null, restaurants = emptyList(), loading = true))
    var state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val restaurants = RestaurantRepository.getAll()

                val user = when(userEmail){
                    null -> null
                    else -> UserRepository.getByEmail(userEmail)
                }
                _state.value = UserHomeScreenState(user = user, restaurants = restaurants, loading = false)
            }
            catch (e: Exception){
                Log.w("HomeViewModel", "Error while fetching data: ${e.message}")
            }
        }
    }
}
