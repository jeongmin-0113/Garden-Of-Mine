package com.example.gardenofmine.ui.model

import gardenofmine.composeapp.generated.resources.Res
import gardenofmine.composeapp.generated.resources.ic_home
import gardenofmine.composeapp.generated.resources.ic_personal
import gardenofmine.composeapp.generated.resources.ic_search
import org.jetbrains.compose.resources.DrawableResource


sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: DrawableResource
) {
    object Home: BottomNavItem("bottom_home", "Home", Res.drawable.ic_home)
    object Search: BottomNavItem("bottom_search", "Search", Res.drawable.ic_search)
    object Personal: BottomNavItem("bottom_personal", "Personal", Res.drawable.ic_personal)
}