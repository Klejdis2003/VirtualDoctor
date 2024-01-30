package com.packages.main.model.user
class User(
    userName: Int,
    name: String,
    email: String,
    password: String,
    userHealthInfo: UserHealthInfo
) : Account(userName, name, email, password) {
}