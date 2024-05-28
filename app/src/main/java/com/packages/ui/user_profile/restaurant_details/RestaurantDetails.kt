package com.packages.ui.user_profile.restaurant_details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.packages.ui.components.ItemDisplay
import com.packages.ui.components.PaddedText


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetails(
    viewModel: RestaurantDetailsViewModel
){
    val state by viewModel.state.collectAsState()
    val restaurant = state.restaurant
    val menu = state.menu
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
            ItemDisplay(items = menu, onButtonClick = {viewModel.addItemToDailyIntake(it)})
        }
    }
}



