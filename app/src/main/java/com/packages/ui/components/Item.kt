package com.packages.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.packages.data.model.restaurant.Item

@Composable
fun MenuItem(item: Item, onButtonClick : () -> Unit = {}){
    Card(modifier = Modifier
        .padding(8.dp)
        .size(200.dp, 300.dp)) {
        AsyncImage(model = item.imageUrl, contentDescription = item.name, contentScale = ContentScale.FillBounds, modifier = Modifier
            .fillMaxHeight(0.5f)
            .fillMaxWidth())
        PaddedCardText(text = item.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        PaddedCardText(text = item.description, style = MaterialTheme.typography.bodyMedium)
        Row(horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Column {
                PaddedCardText(text = "Calories: ${item.nutritionValues.calories}", style = MaterialTheme.typography.bodyMedium)
                PaddedCardText(text = "$${item.price}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            }
            FloatingActionButton(onClick = onButtonClick, modifier = Modifier
                .padding(8.dp)) {
                Icon(Icons.Filled.Add, contentDescription = "Add item")
            }
        }

    }
}