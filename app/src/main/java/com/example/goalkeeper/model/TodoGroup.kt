package com.example.goalkeeper.model

data class TodoGroup(
    val groupId: Int,
    val userId: Int,
    var groupName: String,
    var mainTodo: List<Todo> = listOf(),
    var color: String = ToDoGroupColor.BLUE.toString(),
    var icon: String = ToDoGroupIcon.FAVORITE.toString(),
)
{
    constructor():this (0,0,"groupName")
}