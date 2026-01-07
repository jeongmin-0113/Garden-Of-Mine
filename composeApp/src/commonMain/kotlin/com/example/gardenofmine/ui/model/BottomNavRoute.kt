package com.example.gardenofmine.ui.model

import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavRoute(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val badgeCount: Int,
) {
    //object Home: BottomNavRoute("bottom_home", Icon)
}