package com.packages.main

import com.packages.main.backend.model.user.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.URLProtocol
import io.ktor.http.content.TextContent
import io.ktor.http.path
import io.ktor.util.InternalAPI
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.random.Random
import kotlin.random.nextInt


class ApiRequestTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun testGetUsers() {
        runBlocking {
            launch {
                val client = HttpClient()
                val response = client.get{
                    url{
                        protocol = URLProtocol.HTTP
                        host = "localhost:8080"
                        path("users")
                    }
                }
                val jsonString = response.body<String>()
                println(jsonString)
                val users = json.decodeFromString<List<User>>(jsonString)
                println(users)
            }
        }
    }
    @Test
    fun testGetUser() {
        runBlocking {
            launch {
                val client = HttpClient()
                val response = client.get{
                    url{
                        protocol = URLProtocol.HTTP
                        host = "localhost:8080"
                        path("users/1")
                    }
                }
                val jsonString = response.body<String>()
                println(jsonString)
                val user = json.decodeFromString<User>(jsonString)
                println(user)
            }
        }
    }

    @Test
    fun testGetUserByUsername() {
        runBlocking {
            launch {
                val client = HttpClient()
                val response = client.get{
                    url{
                        protocol = URLProtocol.HTTP
                        host = "localhost:8080"
                        path("users/username/dorothy")
                    }
                }
                val jsonString = response.body<String>()
                println(jsonString)
                val user = json.decodeFromString<User>(jsonString)
                println(user)

            }
        }
    }

    @OptIn(InternalAPI::class)
    @Test
    fun testCreateUser(){
        runBlocking {
            launch {
                val client = HttpClient()
                client.post{
                    url{
                        protocol = URLProtocol.HTTP
                        host = "localhost:8080"
                        path("users")
                    }
                    val jsonString = json.encodeToString(User(
                        username = "egi",
                        age = 23,
                        height = 175.5f,
                        weight = 65f,
                        calorieLimit = 2000,
                        maxSugarContent = 100,
                        maxFatContent = 100,
                        maxProteinContent = 100,
                        isVegetarian = false,
                        isVegan = false
                    ))
                    body = TextContent(text = jsonString, contentType = io.ktor.http.ContentType.Application.Json)
                }

            }
        }

    }
    @OptIn(InternalAPI::class)
    @Test
    fun testCreateUsers(){
        val names: Array<String> = arrayOf(
            "egi", "david", "james", "john", "michael", "robert", "william", "mary", "jennifer", "linda",
            "elizabeth", "susan", "margaret", "dorothy", "lisa", "nancy", "karen", "betty", "helen", "sandra",
            "donna", "carol", "ruth", "sharon", "michelle", "laura", "sarah", "kimberly", "deborah", "jessica",
            "shirley", "cynthia", "angela", "melissa", "brenda", "amy", "anna", "rebecca", "virginia", "kathleen",
            "pamela", "martha", "debra", "amanda", "stephanie", "carolyn", "christine", "marie", "janet", "catherine"
        )
        runBlocking {
            launch {
                val client = HttpClient()
                for (i in 0 ..5){
                    val name = names.random()
                    client.post{
                        url{
                            protocol = URLProtocol.HTTP
                            host = "localhost:8080"
                            path("users")
                        }
                        val jsonString = json.encodeToString(User(
                            username = name,
                            age = Random.nextInt(18..30),
                            height = Random.nextInt(150..200).toFloat(),
                            weight = Random.nextInt(50..100).toFloat() ,
                            calorieLimit = Random.nextInt(1500, 2500),
                            maxSugarContent = 100,
                            maxFatContent = 100,
                            maxProteinContent = 100,
                            isVegetarian = false,
                            isVegan = false
                        ))
                        body = TextContent(text = jsonString, contentType = io.ktor.http.ContentType.Application.Json)
                    }
                }
            }
        }
    }
}
