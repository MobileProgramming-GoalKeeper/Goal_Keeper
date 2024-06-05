package com.example.goalkeeper.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel

class GoalKeeperViewModel(): ViewModel() {
    val userID = "user"
//    val userPassword = "1234"

    var themeColor1 = mutableStateOf(Color.Red)
    var themeColor2 = mutableStateOf(Color.Green)

    fun login(userID: String, userPassword: String): Boolean {
//        return this.userID == userID && this.userPassword == userPassword
        return true
    }

    fun register(userID: String, userPassword: String): Boolean {
        return true
    }
}