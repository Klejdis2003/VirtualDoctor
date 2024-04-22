package com.packages.main.sign_in

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String,
    val username: String?,
    val email: String?,
    val accountState: AccountState = AccountState.EXISTS
)

enum class AccountState {
    EXISTS,
    DOES_NOT_EXIST,
}
