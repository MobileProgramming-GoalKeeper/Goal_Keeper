package com.example.goalkeeper.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(var title: String, var icon: ImageVector, var route: String) {
    object Time : BottomNavItem("Time", Icons.Filled.DateRange, Routes.Time.route)
    object Home : BottomNavItem("Home", Icons.Filled.Home, Routes.Home.route)
    object MyPage : BottomNavItem("My Page", Icons.Filled.Person, Routes.MyPage.route)
}