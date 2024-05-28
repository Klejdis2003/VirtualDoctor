package com.packages.data.repositories

import com.packages.data.model.restaurant.Item
import com.packages.data.model.user.Stats
import com.packages.data.model.user.User
import com.packages.main.utils.HttpClient
import io.ktor.util.StringValues

class UserRepository(
    private val httpClient: HttpClient = HttpClient()
) {
    private val path = "/users"

    suspend fun getAll(): List<User> {
        return httpClient.get<List<User>>(path)!!
    }

    suspend fun get(username: String): User? {
        return httpClient.get<User>("$path/$username")
    }

    suspend fun getByEmail(email: String): User? {
        val params = StringValues.build {
            append("email", email)
        }
        return httpClient.get("$path/find", params)
    }

    suspend fun exists(email: String?): Boolean {
        if(email == null) return false
        return getByEmail(email) != null
    }

    suspend fun createUser(user: User): User? {
        return httpClient.post<User>(path, user)
    }

    suspend fun addUserItem(user: User, item: Item): Stats? {
        return httpClient.postWithDifferentResponseType<Item, Stats>("$path/${user.username}/addItem/${item.id}", item)
    }

    suspend fun getDailyStats(user: User): Stats? {
        return httpClient.get("$path/${user.username}/stats/today")
    }

    suspend fun clearTable(key: String): String? {
        val response = httpClient.delete<String>("users/clear/key= $key")
        return response
    }
}