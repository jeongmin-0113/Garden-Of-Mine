package com.example.gardenofmine.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import org.jetbrains.compose.resources.painterResource

/**
 * ===========================================
 * 사이드 네비게이션 바 (Navigation Rail)
 * ===========================================
 *
 * 데스크톱/태블릿 화면에서 표시되는 사이드 네비게이션입니다.
 * 화면 너비가 넓을 때 (WindowWidthSizeClass.Medium 이상) 표시됩니다.
 *
 * 특징:
 * - 기본 상태: 아이콘만 표시 (72dp 너비)
 * - 호버 상태: 아이콘 + 라벨 표시 (160dp 너비)
 * - 부드러운 확장/축소 애니메이션
 *
 * @param navController 네비게이션 컨트롤러
 */
@Composable
fun SideNavigationBar(
    navController: NavController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val navItems = listOf(NavItem.Personal, NavItem.Home, NavItem.Search)

    // 호버 상태 감지
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    // 너비 애니메이션
    val width by animateDpAsState(
        targetValue = if (isHovered) 160.dp else 72.dp,
        label = "navigationWidth"
    )

    NavigationRail(
        modifier = Modifier
            .width(width)
            .hoverable(interactionSource = interactionSource),
        containerColor = Color.Green.copy(alpha = 0.3f),
        contentColor = Color.Black,
    ) {
        val currentRoute = navBackStackEntry?.destination?.route

        navItems.forEach { item ->
            NavigationRailItem(
                icon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        // 아이콘
                        Icon(
                            painter = painterResource(item.icon),
                            contentDescription = item.title
                        )

                        // 라벨 (호버 시 표시)
                        AnimatedVisibility(
                            visible = isHovered,
                            enter = fadeIn() + expandHorizontally(),
                            exit = fadeOut() + shrinkHorizontally()
                        ) {
                            Row(horizontalArrangement = Arrangement.Start) {
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = item.title,
                                    softWrap = false,
                                    maxLines = 1,
                                    overflow = TextOverflow.Clip
                                )
                            }
                        }
                    }
                },
                label = null,  // 커스텀 레이아웃 사용으로 기본 라벨 비활성화
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
