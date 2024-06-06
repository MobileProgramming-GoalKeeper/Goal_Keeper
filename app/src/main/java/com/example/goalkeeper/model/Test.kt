package com.example.goalkeeper.model

import java.time.LocalDateTime

class TestTodoPrint {
    var todoEX = Todo(
        todoId = 1,
        groupId = 1,
        todoName = "치과가기",
        todoDate = LocalDateTime.of(2024, 6, 2, 0, 0).toStringFormat(),
        todoStartAt = LocalDateTime.of(2024, 6, 2, 9, 0).toStringFormat(),
        todoEndAt = LocalDateTime.of(2024, 6, 2, 10, 30).toStringFormat(),
        todoAlart = false,
        todoMemo = "사랑니 빼야됨 ㅜㅜ",
        todoDone = false
    )

    var mainTodo = listOf(
        todoEX,
        todoEX.copy(todoId = 2, todoName = "병원가기", todoMemo = "감기약 타오기"),
        todoEX.copy(todoId = 3, todoName = "약속 잡기", todoMemo = "친구 만나기"),
        todoEX.copy(todoId = 4, todoName = "운동하기", todoMemo = "헬스장 가기")
    )

    var exTodoGroup = TodoGroup(
        groupId = 1,
        userId = 1,
        groupName = "공부",
        mainTodo = mainTodo,
        color = ToDoGroupColor.RED,
        icon = ToDoGroupIcon.PENCIL
    )
}