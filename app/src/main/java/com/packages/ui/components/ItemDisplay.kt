package com.packages.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.packages.data.model.restaurant.Item

@Composable
fun ItemDisplay(items: List<Item>, onButtonClick: (Item) -> Unit = {}) {
    val chunks = items.chunked(2)
    chunks.forEach { chunk ->
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
        ) {
            chunk.forEach {
                MenuItem(item = it, onButtonClick = { onButtonClick(it) })
            }
        }
    }
}