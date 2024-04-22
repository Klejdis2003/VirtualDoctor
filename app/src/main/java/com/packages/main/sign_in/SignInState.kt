package com.packages.main.sign_in

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null,
    var sheetOpened : Boolean = false
)
