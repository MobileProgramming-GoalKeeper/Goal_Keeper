package com.example.goalkeeper.model

data class UserRoutine(
    val routineNum: Int,
    var userId: Int,
    var routineName: String,
    var routineAlart: Boolean
)
{
    constructor():this(0,0,"noName", false)
}
