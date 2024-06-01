package com.example.goalkeeper.nav

sealed class Routes (val route: String) {
    object Main: Routes("Main")
    object Welcome: Routes("Welcome")
    object Register: Routes("Register")
    object Home: Routes("Home")
    object Time: Routes("Time")
    object MyPage: Routes("MyPage")
    object Statistics: Routes("Statistics")
    object Routine: Routes("Routine")
    object ThemeColor: Routes("ThemColor")
}