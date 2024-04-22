package com.packages.main.owner_profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.packages.client.restaurant.Restaurant


@Composable
fun RestaurantOwnerScreen(ownerViewModel: OwnerViewModel, onSignOut: () -> Unit = {}){
    val state = ownerViewModel.state.collectAsState()


    Box {
        if(state.value.loading)
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    else {
        Row(modifier = Modifier.align(Alignment.TopCenter)) {
            Text(
                text = "Welcome ${state.value.ownerData?.username}",
                modifier = Modifier
                    .padding(16.dp).align(Alignment.CenterVertically),
                fontSize = 24.sp
            )
            Button(onClick = onSignOut, modifier = Modifier.padding(16.dp).align(Alignment.CenterVertically)) {
                Text(text = "Sign Out")
            }
        }
            Restaurants(
                restaurants = state.value.restaurants,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        ExtendedFloatingActionButton(
            onClick = {},
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text(text = "Add Restaurant")
        }
    }
}

@Composable
fun Restaurants(restaurants: List<Restaurant>, modifier: Modifier = Modifier){
    Column(modifier = modifier){
        if(restaurants.isEmpty())
            Text(text = "You don't have any restaurants yet. Add one now!")
        else {
            Text(text = "Your restaurants: ")
            restaurants.forEach {
                Card {
                    Text(text = it.name)
                }
            }
        }
    }
}