package com.packages.ui.user_profile

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.packages.data.model.restaurant.Restaurant
import com.packages.ui.user_profile.dashboard.Dashboard
import com.packages.ui.user_profile.restaurant_details.RestaurantDetails
import com.packages.ui.user_profile.restaurant_details.RestaurantDetailsViewModel
import com.packages.ui.user_profile.restaurant_details.RestaurantDetailsViewModelFactory
import com.packages.ui.user_profile.suggestions.Suggestions
import com.packages.ui.user_profile.suggestions.SuggestionsViewModel
import com.packages.ui.user_profile.suggestions.SuggestionsViewModelFactory
import com.packages.virtual_doctor.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSignOut: () -> Unit,
    homeViewModel: HomeViewModel
) {
    val state by homeViewModel.state.collectAsState()
    val userData = state.user
    val navController = rememberNavController()
    val context = LocalContext.current

    Box {
        LaunchedEffect(key1 = state.updatedStatsState) {
            when(state.updatedStatsState){
                is UpdateStatsState.Success -> Toast.makeText(context, "Added item to your daily consumption.", Toast.LENGTH_SHORT).show()
                is UpdateStatsState.Error -> Toast.makeText(context, "Error contacting server", Toast.LENGTH_SHORT).show()
                is UpdateStatsState.NotStarted -> {
                    // Do nothing
                }
            }
            homeViewModel.onStatsToastDismissed()
        }
        CenterAlignedTopAppBar(title = {
           userData?.let {
               Text(text = it.username)
           }}
        )
        if (state.loading)
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

        BottomAppBar(
            actions = {
                      items.forEach() {
                          IconButton(onClick = {
                                navController.navigate(it.route)
                          }) {
                              Icon(
                                  painter = painterResource(id = it.id),
                                  contentDescription = it.route,
                                  modifier = Modifier.clickable {
                                      navController.navigate(it.route) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true

                                      }
                                  }
                              )
                          }
                      }
            },
            floatingActionButton = {
                FloatingActionButton(onClick = onSignOut) {
                    Icon(painter = painterResource(R.drawable.logout), contentDescription = "Log out")
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        NavHost(navController = navController, startDestination = UserScreen.Dashboard.route, modifier = Modifier.align(Alignment.Center)) {
            composable(UserScreen.Dashboard.route) {
                Dashboard(stats = state.stats, nutritionValues = state.user?.nutritionPlan?.nutritionValues)
            }
            composable(UserScreen.Suggestions.route){
                val suggestionsViewModel = viewModel<SuggestionsViewModel>(factory = state.user?.let {
                    SuggestionsViewModelFactory(
                        userNutritionType = it.nutritionType,
                        userNutritionPlan = it.nutritionPlan,
                        itemRepository = homeViewModel.itemRepository
                    )
                })
                Suggestions(suggestionsViewModel)
            }
            navigation(route = UserScreen.Home.route, startDestination = "restaurantList") {

                composable("restaurantList") {
                    RestaurantList(restaurants = state.restaurants, onRestaurantClick = {
                        homeViewModel.onRestaurantClicked(it)
                        navController.navigate("restaurantDetails") {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true

                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    })
                }

                composable("restaurantDetails") {
                    val restaurantDetailsViewModel = viewModel<RestaurantDetailsViewModel>( factory =
                        RestaurantDetailsViewModelFactory(
                            state.clickedRestaurant!!,
                            onAddButtonPressed = { homeViewModel.onAddButtonPressed(it)},
                            homeViewModel.restaurantRepository
                        )
                    )
                    RestaurantDetails(viewModel = restaurantDetailsViewModel)
                }
            }
        }
    }
}

@Composable
fun RestaurantList(
    modifier: Modifier = Modifier,
    restaurants: List<Restaurant>,
    onRestaurantClick : (Restaurant) -> Unit = {}
) {
    var cardExpanded by remember { mutableStateOf("") }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .size(600.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Restaurants you might like: ",
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.Start),
            fontSize = MaterialTheme.typography.headlineMedium.fontSize
        )
        restaurants.forEach {
            Card(modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .clickable {
                    onRestaurantClick(it)
                }) {
                Text(
                    text = it.name,
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.Start),
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.labelLarge.fontSize
                )
                Text(
                    text = "Location: ${it.streetAddress}",
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.End)
                )
            }
        }
    }
}


