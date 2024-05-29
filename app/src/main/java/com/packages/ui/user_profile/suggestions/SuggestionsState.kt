package com.packages.ui.user_profile.suggestions

import com.packages.data.model.restaurant.Item

data class SuggestionsState(
    val itemSuggestions: List<Item> = emptyList(),
    val userNutritionType: String = ""
)
