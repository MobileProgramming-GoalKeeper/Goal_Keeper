package com.example.goalkeeper.model

data class Statistics(
    val userId: Int,
    val postponedNum: Int
)
{
    constructor():this(0,0)
}