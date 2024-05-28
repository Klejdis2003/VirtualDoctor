package com.packages.ui.owner_profile.restaurant.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.packages.data.model.restaurant.Restaurant
import com.packages.data.model.restaurant.RestaurantOwner
import com.packages.data.repositories.RestaurantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RestaurantRegistrationViewModel(
    private val owner: RestaurantOwner?,
    private val restaurantRepository: RestaurantRepository,
    val redirect: () -> Unit = {}
    ): ViewModel() {

    private val _state = MutableStateFlow(RestaurantRegistrationState())
    val state = _state.asStateFlow()

    fun onFieldChange(fieldName: String, value: String) {
        val state = _state.value
        when(fieldName) {
            "Name" -> _state.value = state.copy(name = value)
            "Street Address" -> _state.value = state.copy(streetAddress = value)
            "City" -> _state.value = state.copy(city = value)
            "Postcode" -> _state.value = state.copy(postcode = value)
            "Country" -> _state.value = state.copy(country = value)
            "Telephone" -> _state.value = state.copy(telephone = value)
            "Email" -> _state.value = state.copy(email = value)
            "Website" -> _state.value = state.copy(website = value)
        }
        _state.value = _state.value.copy(fields = state.fields.map {
            if(it.label == fieldName) {
                it.copy(value = value)
            } else {
                it
            }
        })
    }

    fun isFormValid(): Boolean {
        val state = _state.value
        return formErrors().isEmpty()
    }

    fun formErrors(): List<String> {
        val state = _state.value
        val errors = mutableListOf<String>()
        if(state.name.isBlank()) errors.add("Name cannot be blank")
        if(state.streetAddress.isBlank()) errors.add("Street Address cannot be blank")
        if(!state.city.matches(Regex("^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*\$"))) errors.add("City cannot be blank and should only containt letters")
        if(!state.postcode.matches(Regex("^[0-9]{5}\$"))) errors.add("Postcode should consist of 5 digits")
        if(!state.country.matches(Regex("^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*\$"))) errors.add("Country cannot be blank and should only contain letters")
        if(!state.telephone.matches(Regex("^[0-9]{10}\$"))) errors.add("Telephone should consist of 10 digits")
        if(!state.email.matches(Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"))) errors.add("Email should be in the standard format, e.g. a@b.com")
        if(state.website.isBlank()) errors.add("Website cannot be blank")
        return errors
    }

    fun onSubmit() {
        viewModelScope.launch {
            restaurantRepository.create(
                Restaurant(
                    name = state.value.name,
                    streetAddress = state.value.streetAddress,
                    city = state.value.city,
                    postcode = state.value.postcode,
                    country = state.value.country,
                    telephone = state.value.telephone,
                    email = state.value.email,
                    website = state.value.website,
                    owner = owner
                )
            )
            redirect()
        }
    }
}

class RestaurantRegistrationViewModelFactory(
    private val owner: RestaurantOwner?,
    private val restaurantRepository: RestaurantRepository,
    private val redirect: () -> Unit = {}
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RestaurantRegistrationViewModel::class.java)) {
            return RestaurantRegistrationViewModel(owner, restaurantRepository, redirect) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}