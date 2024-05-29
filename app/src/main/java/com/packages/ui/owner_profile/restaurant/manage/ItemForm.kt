package com.packages.ui.owner_profile.restaurant.manage

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.packages.data.model.restaurant.Ingredient
import com.packages.ui.components.FormTemplate
import com.packages.ui.components.InputField
import com.packages.ui.components.PaddedText

@Composable
fun ItemForm(
    viewModel: ItemFormViewModel
) {
    var currentScreen by remember { mutableStateOf<ItemFormParts>(ItemFormParts.Part1) }
    val state = viewModel.state.collectAsStateWithLifecycle()
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.6f)) {
        Text(text = "Part ${currentScreen.part} of ${ItemFormParts.count}", modifier = Modifier.align(
            Alignment.CenterHorizontally))
        LinearProgressIndicator(
            progress = { currentScreen.part.toFloat() / ItemFormParts.count.toFloat()},
            modifier = Modifier.fillMaxWidth()
        )
        RenderItemFormPart(
            currentScreen = currentScreen,
            fields1 = state.value.fields1,
            fields2 = state.value.fields2,
            part3State = state.value.form3,
            updateCurrentScreen = { currentScreen = it },
            onFieldChange = viewModel::onFieldChange,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            viewModel = viewModel
        )
    }
}

@Composable
fun RenderItemFormPart(
    modifier: Modifier = Modifier,
    currentScreen: ItemFormParts,
    fields1: List<InputField>,
    fields2: List<InputField>,
    part3State: Form3State,
    updateCurrentScreen: (ItemFormParts) -> Unit,
    onFieldChange: (String, String) -> Unit,
    viewModel: ItemFormViewModel
) {
    when (currentScreen) {
        ItemFormParts.Part1 -> FormTemplate(
            inputFields = fields1,
            onSubmit = { updateCurrentScreen(ItemFormParts.Part2) },
            onChanged = { label: String, value: String -> onFieldChange(label, value) },
            isValid = viewModel.isPart1Valid(),
            modifier = modifier,
            buttonText = "Next"
        )

        ItemFormParts.Part2 -> Part2(
            fields2 = fields2 ,
            onFieldChange = onFieldChange ,
            onSubmit = { updateCurrentScreen(ItemFormParts.Part3)},
            isValid =  viewModel.isPart2Valid(),
            modifier = modifier,
            backAction = { updateCurrentScreen(ItemFormParts.Part1) }
        )

        ItemFormParts.Part3 -> Part3(
            state = part3State,
            onQueryChange = {(viewModel::onSearchQueryChange)(it)},
            onSearch = { it -> viewModel.onSearch()},
            onActiveChange = { viewModel.onSearch() },
            onButtonClick = { viewModel.onIngredientAdd(it) },
            backAction = { updateCurrentScreen(ItemFormParts.Part2) },
            onSubmit = viewModel::onFinalSubmit,
        )
    }

}

@Composable
fun Part2 (
    fields2: List<InputField>,
    onFieldChange: (String, String) -> Unit,
    onSubmit: () -> Unit,
    isValid: Boolean,
    modifier: Modifier = Modifier,
    backAction: () -> Unit = {}
) {
    BackHandler {
        backAction()
    }
    FormTemplate(
        inputFields = fields2,
        onSubmit = onSubmit,
        onChanged = onFieldChange,
        isValid = isValid,
        modifier = modifier,
        buttonText = "Next"
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Part3(
    state: Form3State,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onActiveChange: (Boolean) -> Unit,
    onButtonClick: (Ingredient) -> Unit = {},
    onSubmit: () -> Unit,
    context: Context = LocalContext.current,
    backAction: () -> Unit = {}
    ) {
    BackHandler {
        backAction()
    }
    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        query = state.query,
        onQueryChange = onQueryChange ,
        onSearch = onSearch ,
        active = state.isSearchActive ,
        trailingIcon = {Icon(Icons.Filled.Search, contentDescription = null)},
        onActiveChange = onActiveChange) {

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize()){
            items(
                count = state.result.size,
                itemContent = { index ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text =
                            "${state.result[index].name} " +
                                    "(${
                                        state.result[index].type.lowercase()
                                            .replaceFirstChar { it.uppercase() }
                                    }" + ")",

                            )
                        IconButton(onClick = {
                            onButtonClick(state.result[index])
                            Toast.makeText(context, "Ingredient added", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(Icons.Filled.Add, contentDescription = null)
                        }
                    }
                    HorizontalDivider(thickness = 2.dp, modifier = Modifier.padding(top = 8.dp))
                }


            )
        }
    }

    PaddedText(text = "Selected Ingredients", style = MaterialTheme.typography.titleMedium)
    state.selectedIngredients.forEach {
        PaddedText(text = it.name)
        HorizontalDivider()
    }
    Button(onClick = {onSubmit()}, modifier = Modifier
        .fillMaxWidth()
        .padding(top = 100.dp, start = 50.dp, end = 50.dp)) {
        Text(text = "Submit")
    }
}





sealed class ItemFormParts(val part: Int) {
    data object Part1 : ItemFormParts(1)
    data object Part2 : ItemFormParts(2)
    data object Part3 : ItemFormParts(3)

    companion object{
        var count = ItemFormParts::class.sealedSubclasses.size
    }
}