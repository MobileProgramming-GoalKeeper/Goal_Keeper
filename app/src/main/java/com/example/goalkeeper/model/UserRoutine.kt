package com.example.goalkeeper.model

data class UserRoutine(
    var routineName: String,
    var routineAlert: Boolean
)
{
    constructor():this("noName", false)
}
