package com.example.gardenofmine.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.gardenofmine.ui.model.BottomNavItem
import com.example.gardenofmine.ui.model.BottomNavigationBar
import com.example.gardenofmine.ui.model.Navigation
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val navController = rememberNavController()
    MaterialTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            bottomBar = {
                BottomNavigationBar(navController)
            }
        ) {
            Navigation(navController)
        }
    }
}