package com.packages.main.backend.model.user

import kotlinx.serialization.Serializable

@Serializable
open class Account(
    val userName: Int,
    val name: String,
    val email: String,
)