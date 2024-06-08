package com.example.goalkeeper.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


data class Todo (
    var todoId: String,
    var groupId: String,
    var todoName: String,
    var todoDate: String,
    var todoStartAt: String?=null,
    var todoEndAt: String?=null,
//    var todoTime: String,
    var todoAlert: Boolean,
    var todoMemo: String,
    var todoDone: Boolean,
    val childTodo: List<Todo>? = listOf(),
    var postponedNum: Int=0,
)
{
    constructor():this("0","0","todoName", "2024년 06월 13일",null,null,false,"memo",false,null, 0)
    fun formatDateTime(dateTime: LocalDateTime?, displayTime: Boolean): String {
        return if(displayTime) {
            dateTime?.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm")) ?: ""
        }else   {
            dateTime?.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")) ?: ""
        }
    }
    fun toText(): String {
        return """
            할일: ${todoName}
            날짜: ${todoDate}
            메모: ${todoMemo}
            완료 여부: ${if (todoDone) "완료" else "미완료"}
            ${if (childTodo!=null && childTodo.isNotEmpty()) {
            "<서브 할일>: \n"+ childTodo.joinToString("\n") { it.toText() }
            } else ""}    
        """.trimIndent()
    }
}

