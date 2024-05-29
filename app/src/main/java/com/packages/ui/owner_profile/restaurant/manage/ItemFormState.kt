package com.packages.ui.owner_profile.restaurant.manage

import com.packages.data.model.restaurant.Ingredient
import com.packages.ui.components.InputField

data class ItemFormState(
    val name : String = "",
    val description : String = "",
    val price : String = "",
    val imageUrl : String = "",
    val type: String = "",
    val calories: String = "",
    val protein: String = "",
    val fat: String = "",
    val carbohydrates: String = "",
    val ingredients: List<Ingredient> = emptyList(),
    val fields1: List<InputField> = emptyList(),
    val fields2: List<InputField> = emptyList(),
    val form3 : Form3State = Form3State(),
)

data class Form3State(
    val query: String = "",
    val isSearchActive: Boolean = false,
    val result: List<Ingredient> = emptyList(),
    val selectedIngredients: MutableList<Ingredient> = mutableListOf()
)
