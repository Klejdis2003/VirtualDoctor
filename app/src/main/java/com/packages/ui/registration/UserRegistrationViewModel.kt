package com.packages.ui.registration

import android.util.Log
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.packages.data.model.nutrition.NutritionPlan
import com.packages.data.model.nutrition.NutritionType
import com.packages.data.model.user.User
import com.packages.data.repositories.NutritionRepository
import com.packages.data.repositories.UserRepository
import com.packages.ui.components.InputField
import com.packages.ui.components.SelectInputField
import com.packages.ui.components.TextInputField
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UserRegistrationViewModel(
    private val email: String,
    private val userRepository: UserRepository,
    private val nutritionRepository: NutritionRepository,
    private val redirect: () -> Unit
): ViewModel() {
    private val _state = MutableStateFlow(UserRegistrationState())
    private lateinit var _nutritionTypes: List<NutritionType>
    private lateinit var _nutritionPlans: List<NutritionPlan>

    init {
        runBlocking {
            launch {
                _nutritionTypes = nutritionRepository.getAllNutritionTypes()
                _nutritionPlans = nutritionRepository.getAllNutritionPlans()
                Log.w("UserRegistrationViewModel", "init: $_nutritionTypes")
            }
        }
        initFields()

    }

    val state = _state.asStateFlow()

    private fun initFields() {
        val fields: List<InputField> = listOf(
            TextInputField("Username", "", KeyboardType.Text, true),
            TextInputField("Age", "", KeyboardType.Number, true),
            TextInputField("Height", "", KeyboardType.Number, true),
            TextInputField("Weight", "", KeyboardType.Number, true),
            SelectInputField("Nutrition Type", "", _nutritionTypes.map { it.name }, true),
            SelectInputField("Nutrition Plan", "", _nutritionPlans.map { it.name }, true)
        )
        _state.value = _state.value.copy(fields = fields)
    }

    fun isFormValid(): Boolean {
        val state = _state.value
        return state.username.isNotBlank() &&
                state.age != null &&
                state.height != null &&
                state.weight != null &&
                state.selectedNutritionType.isNotBlank() &&
                state.selectedNutritionPlan.isNotBlank()
    }

    fun onFieldChange(label: String, value: String) {
        val state = _state.value
        val newState = when(label) {
            "Username" -> state.copy(username = value)
            "Age" -> state.copy(age = value.toIntOrNull())
            "Height" -> state.copy(height = value.toIntOrNull())
            "Weight" -> state.copy(weight = value.toIntOrNull())
            "Nutrition Type" -> state.copy(selectedNutritionType = value)
            "Nutrition Plan" -> state.copy(selectedNutritionPlan = value)
            else -> state
        }
        _state.value = newState
        _state.value = _state.value.copy(fields = _state.value.fields.map {
            if(it.label == label) it.copy(value = value) else it
        })
        Log.w("UserRegistrationViewModel", "onFieldChange: ${_state.value}")
    }
    fun onFormSubmit() {
        val state = _state.value
        viewModelScope.launch {
            val nutritionType = _nutritionTypes.find { it.name == state.selectedNutritionType }
            val nutritionPlan = _nutritionPlans.find { it.name == state.selectedNutritionPlan }
            if(nutritionType == null || nutritionPlan == null) {
                Log.e("UserRegistrationViewModel", "Nutrition Type Name: ${state.selectedNutritionType}")
                return@launch
            }
            userRepository.createUser(
                User(
                    email = email,
                    username = state.username,
                    age = state.age!!,
                    height = state.height!!,
                    weight = state.weight!!,
                    nutritionType = nutritionType,
                    nutritionPlan = nutritionPlan
                )
            )
        }
        redirect()
    }

}

class UserRegistrationViewModelFactory(
    private val email: String,
    private val userRepository: UserRepository,
    private val nutritionRepository: NutritionRepository,
    private val redirect: () -> Unit) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserRegistrationViewModel::class.java)) {
            return UserRegistrationViewModel(email, userRepository, nutritionRepository, redirect) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}