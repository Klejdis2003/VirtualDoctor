package com.packages.data.repositories

import com.packages.data.model.restaurant.Ingredient
import com.packages.data.model.restaurant.Item
import com.packages.main.utils.HttpClient
import io.ktor.util.StringValues

class ItemRepository(private val httpClient: HttpClient = HttpClient()) {
    private val base = "/items"
    suspend fun getAll(params: StringValues = StringValues.Empty): List<Item> {
        return httpClient.get(base, params)!!
    }

    suspend fun get(id: Int): Item? {
        return httpClient.get("$base/$id")
    }

    suspend fun filterByNutritionType(nutritionType: String): List<Item> {
        return getAll(StringValues.build {
            append("nutritionType", nutritionType)
        })
    }

    suspend fun searchIngredients(query: String): List<Ingredient> {
        return httpClient.get("$base/ingredients", StringValues.build {
            append("name", query)
        })!!
    }

}