package com.packages.data.repositories

import com.packages.data.model.nutrition.NutritionPlan
import com.packages.data.model.nutrition.NutritionType
import com.packages.main.utils.HttpClient

class NutritionRepository(private val httpClient: HttpClient = HttpClient()) {
    private val baseUrl = "/nutrition"

    suspend fun getAllNutritionPlans(): List<NutritionPlan> {
        return httpClient.get("$baseUrl/plans")!!
    }

    suspend fun getAllNutritionTypes(): List<NutritionType> {
        return httpClient.get("$baseUrl/types")!!
    }

}
