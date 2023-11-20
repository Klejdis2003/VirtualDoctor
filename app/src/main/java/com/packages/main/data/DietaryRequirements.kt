package com.packages.main.data

data class DietaryRequirements (
    var calorieLimit: Int,
    var maxSugarContent: Int,
    var maxFatContent: Int,
    var maxProteinContent: Int,
    var vegetarian: Boolean = false,
    var vegan: Boolean = false,
    var glutenFree: Boolean = false,
    var nutFree: Boolean = false,
    var dairyFree: Boolean = false,
    var halal: Boolean = false,
    var kosher: Boolean = false,
    )

