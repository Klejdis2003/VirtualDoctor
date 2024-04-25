package com.packages.user_profile.restaurant_details

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.packages.client.restaurant.Item


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetails(
    viewModel: RestaurantDetailsViewModel
){
    val state by viewModel.state.collectAsState()
    val restaurant = state.restaurant
    val menu = state.menu
    val chunks = menu.chunked(2)
    Scaffold(
        topBar = {
             TopAppBar(title = {
                 Text(text = restaurant.name)
             },
                 modifier = Modifier.padding(top = 20.dp)
             )
        }
    ) {paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            PaddedText(text = "${restaurant.city}, ${restaurant.country}", style = MaterialTheme.typography.bodyLarge)
            PaddedText(text = "${restaurant.streetAddress}, ${restaurant.postcode}")

            PaddedText(text = "Menu", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 20.dp))
            chunks.forEach { chunk ->
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                ) {
                    chunk.forEach {
                        MenuItem(item = it, onButtonClick = {
                            viewModel.addItemToDailyIntake(it)
                        })
                    }
                }
            }
        }
    }
}

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
                PaddedCardText(text = "Calories: ${item.calories}", style = MaterialTheme.typography.bodyMedium)
                PaddedCardText(text = "$${item.price}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            }
            FloatingActionButton(onClick = onButtonClick, modifier = Modifier
                .padding(8.dp)) {
                Icon(Icons.Filled.Add, contentDescription = "Add item")
            }
        }

    }
}

@Composable
fun PaddedText(text: String, modifier: Modifier = Modifier, style: TextStyle = MaterialTheme.typography.bodyMedium, fontWeight: FontWeight = FontWeight.Normal){
    Text(text = text, style = style, modifier = modifier.padding(start = 16.dp, bottom = 3.dp, top = 3.dp), fontWeight = fontWeight)
}

@Composable
fun PaddedCardText(text: String, modifier: Modifier = Modifier, style: TextStyle = MaterialTheme.typography.bodyMedium, fontWeight: FontWeight = FontWeight.Normal){
    Text(text = text, style = style, modifier = Modifier.padding(start = 8.dp, top = 3.dp), fontWeight = fontWeight)
}