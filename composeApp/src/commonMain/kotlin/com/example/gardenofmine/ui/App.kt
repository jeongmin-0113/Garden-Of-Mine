package com.example.gardenofmine.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
import com.example.gardenofmine.ui.navigation.AppNavigation
import com.example.gardenofmine.ui.navigation.BottomNavigationBar
import com.example.gardenofmine.ui.navigation.SideNavigationBar
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * ===========================================
 * Garden of Mine - 메인 앱 컴포저블
 * ===========================================
 *
 * 앱의 루트 컴포저블로, 전체 레이아웃과 네비게이션을 관리합니다.
 *
 * 반응형 디자인:
 * - 좁은 화면 (모바일): 하단 네비게이션 바
 * - 넓은 화면 (태블릿/데스크톱): 사이드 네비게이션 레일
 */
@Composable
@Preview
fun App() {
    val navController = rememberNavController()

    MaterialTheme {
        // 현재 창 크기 확인
        val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
        val isWideScreen = windowSizeClass.isWidthAtLeastBreakpoint(
            WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
        )

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            bottomBar = {
                // 모바일에서만 하단 네비게이션 표시
                if (!isWideScreen) {
                    BottomNavigationBar(navController)
                }
            }
        ) {
            if (isWideScreen) {
                // 태블릿/데스크톱: 사이드 네비게이션 + 콘텐츠
                Row {
                    SideNavigationBar(navController)
                    AppNavigation(navController)
                }
            } else {
                // 모바일: 콘텐츠만
                AppNavigation(navController)
            }
        }
    }
}
