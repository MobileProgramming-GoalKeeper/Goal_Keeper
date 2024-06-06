package com.example.goalkeeper.model

data class TodoGroup(
    val groupId: Int,
    val userId: Int,
    var groupName: String,
    var mainTodo: List<Todo> = listOf(),
    var color: ToDoGroupColor = ToDoGroupColor.BLUE,
    var icon: ToDoGroupIcon = ToDoGroupIcon.FAVORITE,
)
{
    constructor():this (0,0,"groupName")
}