package com.packages.ui.user_profile.suggestions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.packages.data.model.nutrition.NutritionPlan
import com.packages.data.model.nutrition.NutritionType
import com.packages.data.model.restaurant.Item
import com.packages.data.repositories.ItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SuggestionsViewModel(
    private val userNutritionType: NutritionType,
    val userNutritionPlan: NutritionPlan,
    private val itemRepository: ItemRepository,
    val onAddButtonClicked: (item: Item) -> Unit = {}
    ): ViewModel() {
    private var _state = MutableStateFlow(SuggestionsState(userNutritionType = userNutritionType.name))
    var state = _state.asStateFlow()


    init {
        updateState()
    }

    fun updateState() {
        viewModelScope.launch {
            val items = itemRepository.filterByNutritionType(userNutritionType.name)
            _state.value = _state.value.copy(itemSuggestions = items)
        }
    }
}

class SuggestionsViewModelFactory(
    private val userNutritionType: NutritionType,
    private val userNutritionPlan: NutritionPlan,
    private val itemRepository: ItemRepository,
    private val onAddButtonClicked: (item: Item) -> Unit = {}
    ) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SuggestionsViewModel::class.java)) {
            return SuggestionsViewModel(userNutritionType, userNutritionPlan, itemRepository, onAddButtonClicked) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}