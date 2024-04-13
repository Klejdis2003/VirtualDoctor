package com.packages.main.services

import com.packages.client.restaurant.RestaurantOwner
import com.packages.main.utils.HttpRequestUtil
import io.ktor.http.HttpStatusCode
import io.ktor.util.StringValues
import kotlinx.serialization.encodeToString
import java.util.Optional

class RestaurantOwnerService {

    companion object {
        private val baseUrl = "/restaurant_owners"

        /**
         * @param id the id of the restaurant owner that needs to be retrieved
         * @return a list of all restaurant owners
         */
        suspend fun getRestaurantOwner(id: Int): RestaurantOwner? {
            val url = "$baseUrl/$id"
            val restaurantOwnerJson = HttpRequestUtil.getJsonFromRequest(url)
            return if (restaurantOwnerJson == null) null
            else HttpRequestUtil.json.decodeFromString<RestaurantOwner>(restaurantOwnerJson)
        }

        /**
         * @param email the email of the restaurant owner that needs to be retrieved
         * @return a list of all restaurant owners
         */
        suspend fun getRestaurantOwner(email: String): RestaurantOwner? {
            val url = "$baseUrl/find"
            val params = StringValues.build {
                append("email", email)
            }
            val restaurantOwnerJson = HttpRequestUtil.getJsonFromRequest(url, Optional.of(params))
            return if (restaurantOwnerJson == null) null
            else HttpRequestUtil.json.decodeFromString<RestaurantOwner>(restaurantOwnerJson)
        }

        suspend fun exists(email: String): Boolean {
            return getRestaurantOwner(email) != null
        }

        suspend fun createRestaurantOwner(restaurantOwner: RestaurantOwner): RestaurantOwner? {
            val jsonString = HttpRequestUtil.json.encodeToString(restaurantOwner)
            return if (HttpRequestUtil.makePostRequest(baseUrl, jsonString).status == HttpStatusCode.Created) restaurantOwner
            else null
        }

        suspend fun clearTable(key: String): String {
            val response = HttpRequestUtil.makeDeleteRequest(
                "restaurant_owners/clear/key=$key",
                hostAddress = "localhost:8080"
            )
            return if (response.status == HttpStatusCode.Forbidden) "Invalid key, forbidden operation"
            else "Table cleared successfully"
        }
    }
}