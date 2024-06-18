package com.example.goalkeeper.model

data class SubTodo (
    var subTodoId: String="",
    var rootTodoId: String="",
    var subName: String="",
    var subMemo: String="",
    var subDone: Boolean=false
)