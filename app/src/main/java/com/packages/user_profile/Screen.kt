package com.packages.user_profile

import androidx.annotation.DrawableRes
import com.packages.virtual_doctor.R

sealed class Screen(val route: String, @DrawableRes val id: Int) {
    data object Dashboard : Screen("dashboard", R.drawable.baseline_dashboard_24)
    data object Home : Screen("home", R.drawable.baseline_home_24)
}

val items = listOf(
    Screen.Dashboard,
    Screen.Home
)

