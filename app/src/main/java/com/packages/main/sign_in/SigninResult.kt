package com.packages.main.sign_in
data class SigninResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String,
    val username: String?,
    val email: String?
)

