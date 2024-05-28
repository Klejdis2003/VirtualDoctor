
import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.packages.ui.components.FormTemplate
import com.packages.ui.registration.UserRegistrationViewModel

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun UserRegistrationForm(userRegistrationViewModel: UserRegistrationViewModel){
    val state = userRegistrationViewModel.state.collectAsStateWithLifecycle()
    val fields = state.value.fields
    FormTemplate(fields, userRegistrationViewModel::onFormSubmit, userRegistrationViewModel::onFieldChange, userRegistrationViewModel.isFormValid())
}


data class RegistrationState(
    val username: String = "",
    val age: Int? = 0,
    val height: Float? = null,
    val weight: Float? = null,
)

private fun convertToInt(string: String): Int?{
    return if(string.isBlank()) null else string.toInt()
}






