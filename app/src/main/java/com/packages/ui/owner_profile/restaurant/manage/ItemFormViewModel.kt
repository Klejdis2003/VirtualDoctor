package com.packages.ui.owner_profile.restaurant.manage

import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.packages.data.model.nutrition.NutritionValues
import com.packages.data.model.restaurant.Ingredient
import com.packages.data.model.restaurant.Item
import com.packages.data.model.restaurant.ItemType
import com.packages.data.model.restaurant.Restaurant
import com.packages.data.repositories.ItemRepository
import com.packages.data.repositories.RestaurantRepository
import com.packages.ui.components.SelectInputField
import com.packages.ui.components.TextInputField
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ItemFormViewModel(
    private val restaurant: Restaurant,
    private val itemRepository: ItemRepository,
    private val restaurantRepository: RestaurantRepository,
    private val onSubmitted: () -> Unit = {}
) : ViewModel() {
    private val _state = MutableStateFlow(ItemFormState(
        fields1 = listOf(
            TextInputField("Name", "",  KeyboardType.Text, true),
            TextInputField("Description", "",  KeyboardType.Text, true),
            TextInputField("Price", "",  KeyboardType.Number, true),
            TextInputField("Image URL", "",  KeyboardType.Text, true),
            SelectInputField("Type", "", options = ItemType.entries.map { item ->
                item.name.lowercase().replaceFirstChar {it.titlecase() }}, true)
        ),
        fields2 = listOf(
            TextInputField("Calories", "",  KeyboardType.Decimal, true),
            TextInputField("Protein", "",  KeyboardType.Decimal, true),
            TextInputField("Fat", "",  KeyboardType.Decimal, true),
            TextInputField("Carbohydrates", "",  KeyboardType.Decimal, true),
        )
    ))
    val state = _state.asStateFlow()

    fun onFieldChange(label: String, value: String) {
        val newState = when (label) {
            "Name" -> _state.value.copy(name = value)
            "Description" -> _state.value.copy(description = value)
            "Price" -> _state.value.copy(price = value)
            "Image URL" -> _state.value.copy(imageUrl = value)
            "Type" -> _state.value.copy(type = value)
            "Calories" -> _state.value.copy(calories = value)
            "Protein" -> _state.value.copy(protein = value)
            "Fat" -> _state.value.copy(fat = value)
            "Carbohydrates" -> _state.value.copy(carbohydrates = value)
            else -> _state.value
        }
        _state.value = newState
        _state.value = _state.value.copy(fields1 = _state.value.fields1.map {
            if (it.label == label) {
                it.copy(value = value)
            } else {
                it
            }
        })
        _state.value = _state.value.copy(fields2 = _state.value.fields2.map {
            if (it.label == label) {
                it.copy(value = value)
            } else {
                it
            }
        })
    }



    fun onSearchQueryChange(query: String) {
        _state.value = _state.value.copy(form3 = _state.value.form3.copy(query = query))
        fetchResults()
    }


    fun onSearch() {
        _state.value = _state.value.copy(form3 = _state.value.form3.copy(isSearchActive = state.value.form3.isSearchActive.not()))
    }

    fun onIngredientAdd(ingredient: Ingredient) {
        _state.value.form3.selectedIngredients.add(ingredient)
    }

     fun isPart1Valid(): Boolean {
        return _state.value.name.isNotBlank() &&
                _state.value.description.isNotBlank() &&
                _state.value.price.isNotBlank() &&
                _state.value.imageUrl.isNotBlank() &&
                _state.value.type.isNotBlank()
    }

     fun isPart2Valid(): Boolean {
        return _state.value.calories.isNotBlank() &&
                _state.value.protein.isNotBlank() &&
                _state.value.fat.isNotBlank() &&
                _state.value.carbohydrates.isNotBlank()
    }

    fun isPart3Valid(): Boolean {
        return _state.value.form3.selectedIngredients.isNotEmpty()
    }

    fun isFormValid(): Boolean {
        return isPart1Valid() && isPart2Valid() && isPart3Valid()
    }

    fun onFinalSubmit() {
        val item = _state.value.run {
            Item(
                name = name,
                description = description,
                price = price.toFloat(),
                imageUrl = imageUrl,
                type = ItemType.valueOf(type.uppercase()),
                nutritionValues = NutritionValues(
                    calories = calories.toInt(),
                    protein = protein.toInt(),
                    fat = fat.toInt(),
                    carbohydrates = carbohydrates.toInt()

                ),
                ingredients = form3.selectedIngredients
            )
        }
        viewModelScope.launch {
            restaurantRepository.addItemToMenu(restaurant.id, item)
        }
        onSubmitted()
    }

    private fun fetchResults() {
        viewModelScope.launch {
            val max = 8
            val result = itemRepository.searchIngredients(_state.value.form3.query).take(max)
            _state.value = _state.value.copy(form3 = _state.value.form3.copy(result = result))
        }
    }
}

class ItemFormViewModelFactory(
    private val restaurant: Restaurant,
    private val itemRepository: ItemRepository,
    private val restaurantRepository: RestaurantRepository,
    private val onSubmitted: () -> Unit = {}
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemFormViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ItemFormViewModel(restaurant, itemRepository, restaurantRepository, onSubmitted) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}