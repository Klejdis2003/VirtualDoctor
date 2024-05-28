package com.packages.ui.owner_profile.restaurant.registration

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.packages.ui.components.FormTemplate

@Composable
fun RestaurantRegistration(viewModel: RestaurantRegistrationViewModel, modifier : Modifier = Modifier) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val fields = state.value.fields

    FormTemplate(
        inputFields = fields,
        onSubmit = viewModel::onSubmit,
        onChanged = viewModel::onFieldChange,
        isValid = viewModel.isFormValid(),
        formErrors = viewModel.formErrors(),
        modifier = modifier
    )

}