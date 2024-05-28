package com.packages.ui.registration

import com.packages.ui.components.InputField

data class UserRegistrationState(
    val username: String = "",
    val age: Int? = null,
    val height: Int? = null,
    val weight: Int? = null,
    val selectedNutritionType: String = "",
    val selectedNutritionPlan: String = "",
    val fields : List<InputField> = emptyList()

)