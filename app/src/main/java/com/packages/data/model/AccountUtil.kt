package com.packages.data.model

abstract class AccountUtil {

    companion object {
        private fun isEmailValid(email: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        private fun isPasswordValid(password: String): Boolean {
            return password.length in 8..32 && password.contains(Regex(".*\\d.*"))
        }
    }
}