package com.example.goalkeeper.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

data class UserInfo(
    val userId: String,
    var userPw: String,
    var userName: String,
    var themeColor1: Int = Color.Red.toArgb(),
    var themeColor2: Int = Color.Blue.toArgb()
) {
    constructor():this("noId", "noPw", "noName")
}