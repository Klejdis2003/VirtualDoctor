package com.packages.ui.user_profile.suggestions

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.packages.ui.components.ItemDisplay
import com.packages.ui.components.PaddedText

@Composable
fun Suggestions(suggestionsViewModel: SuggestionsViewModel, modifier: Modifier = Modifier) {
    val state = suggestionsViewModel.state.collectAsState()
    val itemSuggestions = state.value.itemSuggestions

    LaunchedEffect(Unit) {
        suggestionsViewModel.updateState()
    }

    Column(modifier = modifier) {
        if (itemSuggestions.isEmpty()) {
            PaddedText(text = "No items found based on your dietary requirements")
        } else
            PaddedText(text = "${state.value.userNutritionType} diet:", style = MaterialTheme.typography.displayMedium)

        ItemDisplay(items = itemSuggestions, onButtonClick = {
            suggestionsViewModel.onAddButtonClicked(it)
        })
    }
}