package com.packages.ui.user_profile

import androidx.annotation.DrawableRes
import com.packages.virtual_doctor.R

sealed class UserScreen(val route: String, @DrawableRes val id: Int) {
    data object Dashboard : UserScreen("dashboard", R.drawable.baseline_dashboard_24)
    data object Home : UserScreen("home", R.drawable.baseline_home_24)
    data object Suggestions : UserScreen("suggestions", R.drawable.baseline_food_24)
}

val items = listOf(
    UserScreen.Dashboard,
    UserScreen.Home,
    UserScreen.Suggestions
)

