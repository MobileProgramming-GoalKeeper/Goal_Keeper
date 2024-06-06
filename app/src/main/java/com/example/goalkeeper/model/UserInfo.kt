package com.example.goalkeeper.model

data class UserInfo(
    val userId: Int=0,
    var userName: String,
    var userPw: String,
    var postponedNum: Int
) {
    constructor():this(0, "noName", "noPw", 0)
}