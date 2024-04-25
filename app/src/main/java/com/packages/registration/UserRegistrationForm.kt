
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.packages.client.user.User
import com.packages.main.model.user.DietaryRequirements
import com.packages.main.repositories.UserRepository
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun UserRegistrationForm(email: String?, onRegistered: () -> Unit){
    val coroutineScope = rememberCoroutineScope()
    var username by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var calorieLimit by remember { mutableStateOf("") }
    var maxSugarContent by remember { mutableStateOf("") }
    var maxFatContent by remember { mutableStateOf("") }
    var maxProteinContent by remember { mutableStateOf("") }
    var isVegetarian by remember { mutableStateOf(false) }
    var isVegan by remember { mutableStateOf(false) }
    val isFormValid = username.isNotBlank() && age.isNotBlank() && height.isNotBlank() && weight.isNotBlank()
    val context = LocalContext.current

    val onSubmit: () -> Unit = {
        coroutineScope.launch {
            if (!isFormValid){
                Toast.makeText(context, "Please fill in all required fields(*) fields", Toast.LENGTH_SHORT).show()
                return@launch
            }
            val user = UserRepository.createUser(
                User(
                    email = email!!,
                    username = username,
                    age = age.toInt(),
                    height = height.toFloat(),
                    weight = weight.toFloat(),
                    dietaryRequirements = DietaryRequirements(
                        calorieLimit = convertToInt(calorieLimit) ?: Int.MAX_VALUE,
                        maxSugarContent = convertToInt(maxSugarContent) ?: Int.MAX_VALUE,
                        maxFatContent = convertToInt(maxFatContent) ?: Int.MAX_VALUE,
                        maxProteinContent = convertToInt(maxProteinContent) ?: Int.MAX_VALUE,
                        isVegetarian = isVegetarian,
                        isVegan = isVegan
                    )
                )
            )

            if(user != null)
                onRegistered()
            else
                Toast.makeText( context, "Failed to register user", Toast.LENGTH_SHORT).show()
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextFieldWithSpacerAndErrorMessage(
                value = username,
                onValueChange = { username = it },
                label = "Username*",
                errorMessage = if(username.isBlank()) "Username cannot be blank" else null
            )

            TextFieldWithSpacerAndErrorMessage(
                value = age,
                onValueChange = { age = it },
                label = "Age*",
                errorMessage = if(age.isBlank()) "Age cannot be blank" else null,
                isNumerical = true
            )

            TextFieldWithSpacerAndErrorMessage(
                value = height,
                onValueChange = { height = it },
                label = "Height*",
                errorMessage = if(height.isBlank()) "Height cannot be blank" else null,
                isNumerical = true
            )

            TextFieldWithSpacerAndErrorMessage(
                value = weight,
                onValueChange = { weight = it },
                label = "Weight*",
                errorMessage = if(weight.isBlank()) "Weight cannot be blank" else null,
                isNumerical = true
            )

            TextFieldWithSpacer(
                value = calorieLimit,
                onValueChange = { calorieLimit = it },
                label = "Calorie Limit",
                isNumerical = true
            )

            TextFieldWithSpacer(
                value = maxSugarContent,
                onValueChange = { maxSugarContent = it },
                label = "Max Sugar Content",
                isNumerical = true
            )

            TextFieldWithSpacer(
                value = maxFatContent,
                onValueChange = { maxFatContent = it },
                label = "Max Fat Content",
                isNumerical = true
            )

            TextFieldWithSpacer(
                value = maxProteinContent,
                onValueChange = { maxProteinContent = it },
                label = "Max Protein Content",
                isNumerical = true
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Checkbox(
                    checked = isVegetarian,
                    onCheckedChange = { isVegetarian = it }
                )
                Text("Vegetarian")
                Checkbox(
                    checked = isVegan,
                    onCheckedChange = { isVegan = it }
                )
                Text("Vegan")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { onSubmit() }, enabled = isFormValid) {
                Text("Register")
            }
            Text(text = "Fields marked with * are required")
        }
    }
}

@Composable
fun TextFieldWithSpacer(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isNumerical: Boolean = false
){
    TextFieldWithSpacerAndErrorMessage(value = value, onValueChange = onValueChange, label = label, isNumerical = isNumerical)
}

@Composable
fun TextFieldWithSpacerAndErrorMessage(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMessage: String? = null,
    isNumerical : Boolean = false
){
    TextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = if(isNumerical) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default,
        label = { Text(label) },
        singleLine = true,
        supportingText = {
            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))

            }
        }
    )
}

private fun convertToInt(string: String): Int?{
    return if(string.isBlank()) null else string.toInt()
}






