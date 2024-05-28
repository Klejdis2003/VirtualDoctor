package com.packages.ui.owner_profile

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.packages.data.model.restaurant.Restaurant
import com.packages.ui.owner_profile.restaurant.registration.RestaurantRegistration
import com.packages.ui.owner_profile.restaurant.registration.RestaurantRegistrationViewModel
import com.packages.ui.owner_profile.restaurant.registration.RestaurantRegistrationViewModelFactory


@Composable
fun RestaurantOwnerScreen(ownerViewModel: OwnerViewModel, onSignOut: () -> Unit = {}){
    val state = ownerViewModel.state.collectAsState()
    val navController = rememberNavController()

    val onAddRestaurant = {
        navController.navigate("add_restaurant")
    }


    Box {
        if(state.value.loading)
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        else {
            Row(modifier = Modifier.align(Alignment.TopCenter)) {
                Text(
                    text = "Welcome ${state.value.ownerData?.username}",
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterVertically),
                    fontSize = 24.sp
                )
                Button(onClick = onSignOut, modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterVertically)) {
                    Text(text = "Sign Out")
                }
            }
        }
        if(navController.currentDestination?.route == OwnerScreenRoutes.Home.route)
            ExtendedFloatingActionButton(
                onClick = { onAddRestaurant()},
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
            ) {
                Text(text = "Add Restaurant")
            }
        else {
            ExtendedFloatingActionButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Text(text = "Back")
            }
        }

        NavHost(navController = navController, startDestination = OwnerScreenRoutes.Home.route, modifier = Modifier.align(Alignment.Center)) {
            composable(OwnerScreenRoutes.Home.route) {
                Restaurants(restaurants = state.value.restaurants, modifier = Modifier.align(Alignment.Center))
            }
            composable(OwnerScreenRoutes.AddRestaurant.route) {
                val restaurantRegistrationViewModel = viewModel<RestaurantRegistrationViewModel>(factory =
                    RestaurantRegistrationViewModelFactory(
                        ownerViewModel.owner,
                        ownerViewModel.restaurantRepository,
                        redirect = {
                            navController.navigate(OwnerScreenRoutes.Home.route)
                            ownerViewModel.updateState()
                        }
                    )
                )
                RestaurantRegistration(
                    restaurantRegistrationViewModel,
                    modifier = Modifier.align(Alignment.Center))
            }
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

sealed class OwnerScreenRoutes(val route: String){
    data object Home: OwnerScreenRoutes("home")
    data object AddRestaurant: OwnerScreenRoutes("add_restaurant")
}