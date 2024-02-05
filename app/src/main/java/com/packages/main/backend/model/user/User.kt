package com.packages.main.backend.model.user

import com.packages.main.utils.HttpRequestUtil
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

@Serializable
data class User(
    val id: Long = 0,
    val username: String,
    val age: Int,
    val height: Float,
    val weight: Float,
    val calorieLimit: Int,
    val maxSugarContent: Int,
    val maxFatContent: Int,
    val maxProteinContent: Int,
    val isVegetarian: Boolean = false,
    val isVegan: Boolean = false,
){
    companion object {
        @Contextual
        private val client = HttpClient()


        /**
         * @return a list of all users(pacients)
         */
        suspend fun getAll(): List<User> {
            val jsonString = HttpRequestUtil.getJsonFromRequest("users")
            return HttpRequestUtil.json.decodeFromString<List<User>>(jsonString)
        }

        suspend fun getById(id: Int): User {
            val jsonString = HttpRequestUtil.getJsonFromRequest("users/$id")
            return HttpRequestUtil.json.decodeFromString<User>(jsonString)
        }

        /**
         * @param username the username of the user that needs to be retrieved
         * @return User object with the given username
         */
        suspend fun getByUsername(username: String): User {
            val jsonString = HttpRequestUtil.getJsonFromRequest("users/username/$username")
            return HttpRequestUtil.json.decodeFromString<User>(jsonString)
        }

        /**
         * @param user User object (Data Access Object) that needs to be created in the database
         */
        suspend fun createUser(user: User) {
            val jsonString = HttpRequestUtil.json.encodeToString(user)
            HttpRequestUtil.getJsonFromPostRequest("users", jsonString)
        }

        suspend fun clearTable(key: String): String {
            val response = HttpRequestUtil.makeDeleteRequest("users/clear/key = $key")
            return if (response.status == HttpStatusCode.Forbidden) "Invalid key, forbidden operation"
            else "Table cleared successfully"
        }

    }
    override fun toString(): String {
        return "User(username='$username', calorieLimit=$calorieLimit, maxSugarContent=$maxSugarContent, maxFatContent=$maxFatContent, maxProteinContent=$maxProteinContent, isVegetarian=$isVegetarian, isVegan=$isVegan)"
    }
}
