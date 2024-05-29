package com.packages.ui.owner_profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.packages.ui.components.PaddedText
import com.packages.ui.owner_profile.restaurant.manage.ItemForm
import com.packages.ui.owner_profile.restaurant.manage.ItemFormViewModel
import com.packages.ui.owner_profile.restaurant.manage.ItemFormViewModelFactory
import com.packages.ui.owner_profile.restaurant.registration.RestaurantRegistration
import com.packages.ui.owner_profile.restaurant.registration.RestaurantRegistrationViewModel
import com.packages.ui.owner_profile.restaurant.registration.RestaurantRegistrationViewModelFactory
import com.packages.ui.user_profile.RestaurantList
import com.packages.ui.user_profile.restaurant_details.RestaurantDetails
import com.packages.ui.user_profile.restaurant_details.RestaurantDetailsViewModel
import com.packages.ui.user_profile.restaurant_details.RestaurantDetailsViewModelFactory


@Composable
fun RestaurantOwnerScreen(ownerViewModel: OwnerViewModel, onSignOut: () -> Unit = {}){
    val state = ownerViewModel.state.collectAsState()
    val navController = rememberNavController()
    val onAddRestaurant = {
        navController.navigate("add_restaurant") {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }


    Box {
        if(state.value.loading)
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        else {
            Row(modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 30.dp)) {
                PaddedText(
                    text = "Welcome ${state.value.ownerData?.username}",
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    style = MaterialTheme.typography.bodyLarge
                )
                Button(onClick = onSignOut, modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterVertically)) {
                    Text(text = "Sign Out")
                }
            }
        }
        ExtendedFloatingActionButton(
            onClick = { onAddRestaurant()},
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
        ) {
            Text(text = "Add Restaurant")

        }

        NavHost(navController = navController, startDestination = OwnerScreenRoutes.Home.route, modifier = Modifier.align(Alignment.Center)) {
            navigation(route = OwnerScreenRoutes.Home.route, startDestination = OwnerScreenRoutes.Home.RestaurantList.route) {
                composable(OwnerScreenRoutes.Home.RestaurantList.route) {
                    Column {
                        PaddedText(
                            text = "Your restaurants: ",
                            style = MaterialTheme.typography.displayMedium
                        )
                        RestaurantList(
                            restaurants = state.value.restaurants,
                            onRestaurantClick = {
                                ownerViewModel.onRestaurantClick(it)
                                navController.navigate(OwnerScreenRoutes.Home.RestaurantDetails.route)
                            })
                    }
                }

                composable(OwnerScreenRoutes.Home.RestaurantDetails.route) {
                    val restaurantDetailsViewModel = viewModel<RestaurantDetailsViewModel>(factory =
                        RestaurantDetailsViewModelFactory(
                            state.value.clickedRestaurant!!,
                            onAddButtonPressed = {},
                            ownerViewModel.restaurantRepository
                        )
                    )
                    OwnerRestaurantDetails(restaurantDetailsViewModel, onButtonClick = {
                        navController.navigate(OwnerScreenRoutes.Home.RestaurantDetails.ItemForm.route) {

                            launchSingleTop = true
                            restoreState = true
                        }
                    })
                }

                composable(OwnerScreenRoutes.Home.RestaurantDetails.ItemForm.route) {
                    val viewModel = viewModel<ItemFormViewModel>(factory = ItemFormViewModelFactory(
                        restaurant = state.value.clickedRestaurant!!,
                        itemRepository = ownerViewModel.itemRepository,
                        restaurantRepository = ownerViewModel.restaurantRepository,
                        onSubmitted = {
                            navController.navigate(OwnerScreenRoutes.Home.route){
                                popUpTo(
                                    navController.graph.startDestinationId
                                )
                            }
                        }

                    ))
                    ItemForm(viewModel)
                }
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
fun OwnerRestaurantDetails(restaurantDetailsViewModel: RestaurantDetailsViewModel, onButtonClick: () -> Unit = {}) {
    @Composable
    fun AddItemButton() {
        Button(onClick = onButtonClick) {
            Text(text = "Add Item")
        }
    }
    RestaurantDetails(viewModel = restaurantDetailsViewModel, menuRowContent = { AddItemButton() })
}

sealed class OwnerScreenRoutes(val route: String){
    data object Home: OwnerScreenRoutes("home") {
        data object RestaurantDetails: OwnerScreenRoutes("restaurant_details"){
            data object ItemForm: OwnerScreenRoutes("item_form")
        }
        data object RestaurantList: OwnerScreenRoutes("restaurant_list")
    }
    data object AddRestaurant: OwnerScreenRoutes("add_restaurant")
}