package com.packages.main.data.user

import com.packages.main.data.DietaryRequirements

class UserAccount(
    userName: Int,
    name: String,
    email: String,
    password: String) : Account(userName, name, email, password) {
        lateinit var dietaryRequirements: DietaryRequirements
        fun enterDietaryRequirements(dietaryRequirements: DietaryRequirements){
            this.dietaryRequirements = dietaryRequirements
        }
}