package com.packages.main

import com.packages.main.backend.model.user.User
import com.packages.main.services.UserService
import com.packages.main.utils.HttpRequestUtil
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import kotlin.random.Random


class ApiRequestTest {
    private val json = Json { ignoreUnknownKeys = true }

    private fun populateTableWithRandomData(){
        val names: Array<String> = arrayOf(
            "egi", "david", "james", "john", "michael", "robert", "william", "mary", "jennifer", "linda",
            "elizabeth", "susan", "margaret", "dorothy", "lisa", "nancy", "karen", "betty", "helen", "sandra",
            "donna", "carol", "ruth", "sharon", "michelle", "laura", "sarah", "kimberly", "deborah", "jessica",
            "shirley", "cynthia", "angela", "melissa", "brenda", "amy", "anna", "rebecca", "virginia", "kathleen",
            "pamela", "martha", "debra", "amanda", "stephanie", "carolyn", "christine", "marie", "janet", "catherine"
        )
        runBlocking {
            launch{
                UserService.clearTable("2003")
                for(i in 0 .. 4){
                    val randName = names.random()
                    val jsonString = json.encodeToString(User(
                        email = "$randName@10.com",
                        username  = randName,
                        age = Random.nextInt(10, 100),
                        height = Random.nextFloat() * 85 + 15,
                        weight = Random.nextFloat() * 100 + 20,
                        calorieLimit = Random.nextInt(1000, 3000),
                        maxSugarContent = Random.nextInt(0, 100),
                        maxFatContent = Random.nextInt(0, 100),
                        maxProteinContent = Random.nextInt(0, 200),
                        isVegetarian = Random.nextBoolean(),
                        isVegan = Random.nextBoolean()
                    ))
                    HttpRequestUtil.makePostRequest("users", jsonString)
                }
            }
        }
    }


    @Before
    fun initialize(){
        populateTableWithRandomData()
    }

    @Test
    fun testGetUsers() {
        runBlocking {
            launch{
                UserService.getAll().forEach { user -> println(user) }
            }
        }
    }

    @Test
    fun testGetUserByUsername() {
        runBlocking {
            launch {
                val users = UserService.getAll()
                val user1 = "${users[0]}@10.com"
                val actualUser1 = UserService.getByEmail(user1)
                assertEquals(user1, actualUser1)
            }
        }
    }

}
