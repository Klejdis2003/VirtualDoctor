package com.packages.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun FormTemplate(
    inputFields: List<InputField>,
    onSubmit: () -> Unit = {},
    onChanged: (String, String) -> Unit = { _, _ -> },
    isValid : Boolean = true,
    formErrors: List<String> = emptyList(),
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            inputFields.forEach { formField ->
                if (formField.type == InputType.TEXT) {
                    formField as TextInputField
                    TextFieldWithSpacer(
                        value = formField.value,
                        onValueChange = { onChanged(formField.label, it) },
                        label = "${formField.label}${if (formField.isRequired) "*" else ""}",
                        keyboardType = formField.keyboardType
                    )
                } else if (formField.type == InputType.SELECT) {

                    formField as SelectInputField
                    DynamicSelectTextField(
                        selectedValue = formField.value,
                        options = formField.options,
                        label = "${formField.label}${if (formField.isRequired) "*" else ""}",
                        onValueChangedEvent = { onChanged(formField.label, it) },
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }

            Button(onClick = { onSubmit() }, enabled = isValid) {
                Text("Register")
            }
            Text(text = "Fields marked with * are required")

            if (formErrors.isNotEmpty())
                Text(text = formErrors[0], modifier = Modifier.padding(top = 16.dp), color = Color.Red)
        }
    }

}

abstract class InputField(
    val label: String,
    val value: String,
    val isRequired: Boolean,
    val type: InputType,
) {
    abstract fun copy(value: String): InputField
}

enum class InputType {
    TEXT,
    SELECT
}

class TextInputField(
    label: String,
    value: String,
    val keyboardType: KeyboardType,
    isRequired: Boolean,
) : InputField(label, value, isRequired, InputType.TEXT) {
    override fun copy(value: String) = TextInputField(label, value, keyboardType, isRequired)
}

class SelectInputField(
    label: String,
    value: String,
    val options: List<String>,
    isRequired: Boolean,
    val expanded: Boolean = false,
) : InputField(label, value, isRequired, InputType.SELECT) {
    companion object {
        var sampleSelectInputFields = listOf(
            SelectInputField("Gender", "", listOf("Male", "Female"), true)
        )
    }
    override fun copy(value: String) = SelectInputField(label, value, options, isRequired)

    fun copy(
        value: String = this.value,
        options: List<String> = this.options,
        expanded: Boolean = this.expanded
    ) = SelectInputField(label, value, options, isRequired, expanded)
}



@Composable
fun TextFieldWithSpacerAndErrorMessage(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text
){
    TextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        label = { Text(label) },
        singleLine = true,
        supportingText = {
            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it)
            }
        }
    )
}

@Composable
fun TextFieldWithSpacer(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType
){
    TextFieldWithSpacerAndErrorMessage(value = value, onValueChange = onValueChange, label = label, keyboardType = keyboardType)
}

