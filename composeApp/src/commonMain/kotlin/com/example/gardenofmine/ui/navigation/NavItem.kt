package com.example.gardenofmine.ui.navigation

import gardenofmine.composeapp.generated.resources.Res
import gardenofmine.composeapp.generated.resources.ic_home
import gardenofmine.composeapp.generated.resources.ic_personal
import gardenofmine.composeapp.generated.resources.ic_search
import org.jetbrains.compose.resources.DrawableResource

/**
 * ===========================================
 * 네비게이션 아이템 정의
 * ===========================================
 *
 * 하단/사이드 네비게이션에서 사용되는 아이템들을 정의합니다.
 *
 * @param route 네비게이션 경로 (고유 식별자)
 * @param title 표시될 제목
 * @param icon 아이콘 리소스
 */
sealed class NavItem(
    val route: String,
    val title: String,
    val icon: DrawableResource
) {
    /** 홈 화면 - 식물 관리 팁 */
    data object Home : NavItem(
        route = "nav_home",
        title = "Home",
        icon = Res.drawable.ic_home
    )

    /** 검색 화면 - 식물 식별 */
    data object Search : NavItem(
        route = "nav_search",
        title = "Search",
        icon = Res.drawable.ic_search
    )

    /** 개인 화면 - 내 식물 관리 */
    data object Personal : NavItem(
        route = "nav_personal",
        title = "Personal",
        icon = Res.drawable.ic_personal
    )
}
