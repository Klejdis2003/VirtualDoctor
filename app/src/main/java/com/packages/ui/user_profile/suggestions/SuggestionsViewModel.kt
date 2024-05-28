package com.packages.ui.user_profile.suggestions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.packages.data.model.nutrition.NutritionPlan
import com.packages.data.model.nutrition.NutritionType
import com.packages.data.repositories.ItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SuggestionsViewModel(userNutritionType: NutritionType, userNutritionPlan: NutritionPlan, itemRepository: ItemRepository): ViewModel() {
    private var _state = MutableStateFlow(SuggestionsState())
    var state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val itemSuggestions = itemRepository.filterByNutritionType(userNutritionType.name)
                _state.value = _state.value.copy(itemSuggestions = itemSuggestions)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

class SuggestionsViewModelFactory(
    private val userNutritionType: NutritionType,
    private val userNutritionPlan: NutritionPlan,
    private val itemRepository: ItemRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SuggestionsViewModel::class.java)) {
            return SuggestionsViewModel(userNutritionType, userNutritionPlan, itemRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}