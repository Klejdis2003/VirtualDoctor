package com.packages.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.packages.client.restaurant.RestaurantOwner


@Composable
fun RestaurantOwnerScreen(ownerData: RestaurantOwner){
    Box {
        Text(text = "Welcome ${ownerData.username}",
            modifier = Modifier.align(Alignment.TopCenter).padding(16.dp),
            fontSize = 24.sp)
        ExtendedFloatingActionButton(
            onClick = {},
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)) {
            Text(text = "Add Restaurant")
        }
    }
}