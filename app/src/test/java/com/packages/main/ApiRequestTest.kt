package com.packages.main

import com.packages.data.model.user.User
import com.packages.main.utils.HttpClient
import kotlinx.coroutines.runBlocking
import org.junit.Test


class ApiRequestTest {
    private val httpClient = HttpClient()

    @Test
    fun testGetAllUsers() {
        val path = "/users"
        runBlocking {
            val users = httpClient.get<List<User>>(path)
            println(users)
        }
    }
}
