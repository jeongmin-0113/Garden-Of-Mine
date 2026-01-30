package com.example.gardenofmine.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gardenofmine.ui.HomeScreen
import com.example.gardenofmine.ui.PersonalScreen
import com.example.gardenofmine.ui.screen.search.SearchScreen

/**
 * ===========================================
 * 앱 네비게이션 호스트
 * ===========================================
 *
 * 앱의 모든 화면 간 네비게이션을 관리합니다.
 *
 * 화면 구성:
 * - Home: 식물 관리 팁 (시작 화면)
 * - Search: 식물 식별
 * - Personal: 내 식물 관리
 */
@Composable
fun AppNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NavItem.Home.route,
    ) {
        // 홈 화면 - 식물 관리 팁
        composable(NavItem.Home.route) {
            HomeScreen()
        }

        // 검색 화면 - 식물 식별
        composable(NavItem.Search.route) {
            SearchScreen()
        }

        // 개인 화면 - 내 식물 관리
        composable(NavItem.Personal.route) {
            PersonalScreen()
        }
    }
}
