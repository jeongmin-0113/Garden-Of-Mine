package com.example.gardenofmine.ui.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import org.jetbrains.compose.resources.painterResource

/**
 * ===========================================
 * 하단 네비게이션 바
 * ===========================================
 *
 * 모바일 화면에서 표시되는 하단 네비게이션 바입니다.
 * 화면 너비가 좁을 때 (WindowWidthSizeClass.Compact) 표시됩니다.
 *
 * @param navController 네비게이션 컨트롤러
 */
@Composable
fun BottomNavigationBar(
    navController: NavController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val navItems = listOf(NavItem.Personal, NavItem.Home, NavItem.Search)

    NavigationBar(
        modifier = Modifier.height(60.dp),
        containerColor = Color.White,
        contentColor = Color.Black,
        tonalElevation = 3.dp
    ) {
        val currentRoute = navBackStackEntry?.destination?.route

        navItems.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(item.icon),
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
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
