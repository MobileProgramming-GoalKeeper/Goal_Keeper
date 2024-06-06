package com.example.goalkeeper.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.goalkeeper.R
import com.example.goalkeeper.nav.BottomNavItem
import com.example.goalkeeper.nav.Routes
import com.example.goalkeeper.screen.MyPage.MyPageScreen
import com.example.goalkeeper.style.AppStyles.engTitleStyle
import com.example.goalkeeper.style.AppStyles.korTitleStyle

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        topBar = { TopTitleBar(navController) },
        bottomBar = { BottomNavigationBar(navController) }
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            NavHost(
                navController = navController,
                startDestination = Routes.Home.route
            ) {
                composable(Routes.Time.route) { TimeScreen() }
                composable(Routes.Home.route) { HomeScreen() }
                composable(Routes.MyPage.route) { MyPageScreen() }
            }
        }
    }
}


@Composable
fun TopTitleBar(navController: NavHostController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route
    TopAppBar(
        title = {
            when (currentRoute) {
                Routes.Home.route -> Text(
                    text = "Goal Keeper",
                    style = engTitleStyle
                )

                Routes.Time.route -> Text(text = "일정", style = korTitleStyle)
                Routes.MyPage.route -> Text(text = "마이페이지", style = korTitleStyle)
//                else -> "앱 이름"
            }
        },
        backgroundColor = colorResource(id = R.color.violet)
    )
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Time,
        BottomNavItem.Home,
        BottomNavItem.MyPage
    )

    BottomNavigation(
        backgroundColor = colorResource(id = R.color.violet)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        item.icon, contentDescription = item.title,
                        modifier = Modifier.size(50.dp),
                        tint = if (currentRoute == item.route) colorResource(R.color.violet_dark) else colorResource(
                            id = R.color.light_pink
                        )
                    )
                },
//                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}


