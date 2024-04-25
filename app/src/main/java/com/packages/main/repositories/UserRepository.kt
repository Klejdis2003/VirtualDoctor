package com.packages.main.repositories

import com.packages.client.restaurant.Item
import com.packages.client.user.Stats
import com.packages.client.user.User
import com.packages.main.utils.HttpRequestUtil
import io.ktor.http.HttpStatusCode
import io.ktor.util.StringValues
import kotlinx.serialization.encodeToString
import java.util.Optional

abstract class UserRepository {

    companion object{
        private val path = "/users"
        /**
         * @return a list of all users(pacients)
         */
        suspend fun getAll(): List<User> {
            val jsonString = HttpRequestUtil.getJsonFromRequest("/users")
            return if(jsonString == null) emptyList()
            else HttpRequestUtil.json.decodeFromString<List<User>>(jsonString)
        }

        suspend fun getById(id: Int): User? {
            val jsonString = HttpRequestUtil.getJsonFromRequest("users/$id")
            return if (jsonString == null) null
            else HttpRequestUtil.json.decodeFromString<User>(jsonString)
        }



        /**
         * @param username the username of the user that needs to be retrieved
         * @return User object with the given username
         */
        suspend fun getByEmail(email: String): User? {
            val params = StringValues.build {
                append("email", email)
            }
            val jsonString = HttpRequestUtil.getJsonFromRequest("$path/find", Optional.of(params))
            return if (jsonString == null) null
            else HttpRequestUtil.json.decodeFromString<User>(jsonString)
        }

        suspend fun exists(email: String?): Boolean {
            if(email == null) return false
            return getByEmail(email) != null
        }
        /**
         * @param user User object (Data Access Object) that needs to be created in the database
         */
        suspend fun createUser(user: User): User? {
            val jsonString = HttpRequestUtil.json.encodeToString(user)
            return if (HttpRequestUtil.makePostRequest(path, jsonString).status == HttpStatusCode.Created) user
            else null
        }

        suspend fun addUserItem(user: User, item: Item): Stats? {
            val updatedStats = HttpRequestUtil.getJsonFromPostRequest("$path/${user.id}/addItem/${item.id}", "")
            return if (updatedStats == null) null
            else HttpRequestUtil.json.decodeFromString<Stats>(updatedStats)
        }

        suspend fun getDailyStats(user: User): Stats? {
            val jsonString = HttpRequestUtil.getJsonFromRequest("$path/${user.id}/stats/today")
            return if (jsonString == null) null
            else HttpRequestUtil.json.decodeFromString<Stats>(jsonString)
        }

        suspend fun clearTable(key: String): String {
            val response = HttpRequestUtil.makeDeleteRequest("users/clear/key = $key", hostAddress = "localhost:8080")
            return if (response.status == HttpStatusCode.Forbidden) "Invalid key, forbidden operation"
            else "Table cleared successfully"
        }

    }
}