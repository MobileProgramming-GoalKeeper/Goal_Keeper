package com.example.goalkeeper.model

data class TodoGroup(
    val groupNum: Int,
    val userId: Int,
    var groupName: String,
    var mainTodo: List<Todo> = listOf()
)
{
    constructor():this (0, 0,"groupName")
}