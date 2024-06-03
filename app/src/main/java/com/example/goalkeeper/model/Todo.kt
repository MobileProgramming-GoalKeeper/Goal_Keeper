package com.example.goalkeeper.model

data class Todo (
    val todoNum: Int=0,
    var groupNum: Int,
    var todoName: String,
    var todoDate: String,
    var todoTime: String,
    var todoAlart: Boolean,
    var todoMemo: String,
    var todoDone: Boolean
)
{
    constructor():this(0,0,"todoName", "todoDate", "todoTime", false, "todoMemo", false)

}