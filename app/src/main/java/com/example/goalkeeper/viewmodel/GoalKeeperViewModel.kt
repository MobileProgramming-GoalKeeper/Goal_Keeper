package com.example.goalkeeper.viewmodel

import androidx.lifecycle.ViewModel

class GoalKeeperViewModel(): ViewModel() {
//    val userID = "user"
//    val userPassword = "1234"

    fun login(userID: String, userPassword: String): Boolean {
//        return this.userID == userID && this.userPassword == userPassword
        return true
    }

    fun register(userID: String, userPassword: String): Boolean {
        return true
    }
}