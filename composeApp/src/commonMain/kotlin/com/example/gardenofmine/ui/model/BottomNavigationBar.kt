package com.example.gardenofmine.ui.model

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import org.jetbrains.compose.resources.painterResource

@Composable
fun BottomNavigationBar(
    navController: NavController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val navItems = listOf(BottomNavItem.Personal, BottomNavItem.Home, BottomNavItem.Search)

    NavigationBar(
        modifier = Modifier
            .height(60.dp),
        containerColor = Color.White,
        contentColor = Color.Black,
        tonalElevation = 3.dp
    ) {
        val currentRoute = navBackStackEntry?.destination?.route
        navItems.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(painterResource(item.icon), contentDescription = item.title)
                },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}