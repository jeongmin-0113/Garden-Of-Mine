package com.example.gardenofmine.ui.model

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gardenofmine.ui.HomeScreen
import com.example.gardenofmine.ui.PersonalScreen
import com.example.gardenofmine.ui.SearchScreen

@Composable
fun Navigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
    ) {
        composable(BottomNavItem.Home.route) {
            HomeScreen()
        }
        composable(BottomNavItem.Search.route) {
            SearchScreen()
        }
        composable(BottomNavItem.Personal.route) {
            PersonalScreen()
        }
    }
}