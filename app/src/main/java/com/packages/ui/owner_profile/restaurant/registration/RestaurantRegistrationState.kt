package com.packages.ui.owner_profile.restaurant.registration

import androidx.compose.ui.text.input.KeyboardType
import com.packages.ui.components.InputField
import com.packages.ui.components.TextInputField

data class RestaurantRegistrationState(
    val name: String = "",
    val streetAddress: String = "",
    val city: String = "",
    val postcode: String = "",
    val country: String = "",
    val telephone: String = "",
    val email: String = "",
    val website: String = "",
    val fields: List<InputField> = listOf(
        TextInputField("Name", "", KeyboardType.Text, true),
        TextInputField("Street Address", "", KeyboardType.Text, true),
        TextInputField("City", "", KeyboardType.Text, true),
        TextInputField("Postcode", "", KeyboardType.Decimal, true),
        TextInputField("Country", "", KeyboardType.Text, true),
        TextInputField("Telephone", "", KeyboardType.Phone, true),
        TextInputField("Email", "", KeyboardType.Email, true),
        TextInputField("Website", "", KeyboardType.Text, true)
    )
)