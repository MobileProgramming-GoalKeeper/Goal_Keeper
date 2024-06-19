package com.example.goalkeeper.nav

import com.example.goalkeeper.R

sealed class BottomNavItem(var title: String, var icon: Int, var route: String) {
    object Time : BottomNavItem("Time", R.drawable.ic_watch, Routes.Time.route)
    object Home : BottomNavItem("Home", R.drawable.ic_home, Routes.Home.route)
    object MyPage : BottomNavItem("My Page", R.drawable.ic_person, Routes.MyPage.route)
}