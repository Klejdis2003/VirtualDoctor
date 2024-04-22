package com.packages.user_profile

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onSignOut: () -> Unit,
    homeViewModel: HomeViewModel
) {
    val state by homeViewModel.state.collectAsState()
    val userData = state.user
    val restaurants = state.restaurants
    var cardExpanded by remember { mutableStateOf("") }
    Box() {
        if(state.loading)
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        else{
            Text(
                text = "Welcome ${userData?.username}",
                modifier = Modifier.align(Alignment.TopCenter),
                fontSize = MaterialTheme.typography.displaySmall.fontSize
            )

            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
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
                            cardExpanded = when {
                                cardExpanded == "expanded${it.id}" -> ""
                                else -> "expanded${it.id}"
                            }
                        }
                        .animateContentSize(tween(300, easing = LinearOutSlowInEasing))) {
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

                        if (cardExpanded == "expanded${it.id}") {
                            Text(
                                text = "City: ${it.city}",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .align(Alignment.End)
                            )
                            Text(
                                text = "Postcode: ${it.postcode}",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .align(Alignment.End)
                            )
                            Text(
                                text = "Country: ${it.country}",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .align(Alignment.End)
                            )
                            Text(
                                text = "Telephone: ${it.telephone}",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .align(Alignment.End)
                            )
                            Text(
                                text = "Email: ${it.email}",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .align(Alignment.End)
                            )
                            Text(
                                text = "Website: ${it.website}",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .align(Alignment.End)
                            )
                        }
                    }
                }
            }
    }
    Button(
        onClick = onSignOut,
        modifier = Modifier.align(Alignment.BottomCenter)
    ) {
        Text(text = "Sign Out")
    }
}
}
