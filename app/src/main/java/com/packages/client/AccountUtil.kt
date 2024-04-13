package com.packages.client

abstract class AccountUtil {

    companion object {
        private fun isEmailValid(email: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
        private fun isPasswordValid(password: String): Boolean {
            return password.length > 8 && (password.contains(Regex("[0-9]")) || password.contains(Regex("[A-Z]")))
        }
        fun createAccount(email: String, password: String){
            if(!isEmailValid(email) || !isPasswordValid(password))
                return
    }}
}