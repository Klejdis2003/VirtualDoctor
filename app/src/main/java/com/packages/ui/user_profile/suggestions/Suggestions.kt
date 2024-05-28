package com.packages.ui.user_profile.suggestions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.packages.ui.components.ItemDisplay
import com.packages.ui.components.PaddedText

@Composable
fun Suggestions(suggestionsViewModel: SuggestionsViewModel) {
    val state = suggestionsViewModel.state.collectAsState()
    val itemSuggestions = state.value.itemSuggestions

    if(itemSuggestions.isEmpty()){
        PaddedText(text = "No items found based on your dietary requirements")
    }
    else
        PaddedText(text = "Based on your dietary requirements, we suggest the following items:")

    ItemDisplay(items = itemSuggestions, onButtonClick = {

    })
}