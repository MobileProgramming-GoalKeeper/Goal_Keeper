package com.example.goalkeeper.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.time.LocalDate
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.goalkeeper.component.todo.ToDoGroupPrint
import com.example.goalkeeper.component.todo.TodoDetailView
import com.example.goalkeeper.model.ToDoGroupColor
import com.example.goalkeeper.model.ToDoGroupIcon
import com.example.goalkeeper.model.Todo
import com.example.goalkeeper.model.TodoGroup
import com.example.goalkeeper.model.toStringFormat
import java.time.LocalDateTime

@Composable
fun HomeScreen() {
    val navController = rememberNavController()
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
        color = ToDoGroupColor.RED.toString(),
        icon = ToDoGroupIcon.PENCIL.toString()
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            //Calender()
          
            ToDoGroupPrint(toDoGroup = exTodoGroup) { todo ->
                navController.navigate("todoMenu/${todo.todoId}")
            }
            ToDoGroupPrint(toDoGroup = exTodoGroup.copy(
                groupId = 2,
                groupName = "위시리스트",
                color = ToDoGroupColor.PINK.toString(),
                icon = ToDoGroupIcon.SHOPPING_CART.toString()
            )) { todo ->
                navController.navigate("todoMenu/${todo.todoId}")
            }
        }

        NavHost(
            navController = navController,
            startDestination = "todoMenu/{todoId}"
        ) {
            composable(
                route = "todoMenu/{todoId}",
                arguments = listOf(navArgument("todoId") { type = NavType.IntType })
            ) { backStackEntry ->
                val todoId = backStackEntry.arguments?.getInt("todoId")
                if (todoId != null) {
                    val todo = mainTodo.find { it.todoId == todoId }
                    if (todo != null) {
                        TodoDetailView(
                            todo = todo,
                            navController = navController,
                            onTodoChange = { updatedTodo ->
                                mainTodo = mainTodo.map {
                                    if (it.todoId == updatedTodo.todoId) updatedTodo else it
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun Calender(modifier: Modifier = Modifier,
             currentDate: LocalDate = LocalDate.now(),
             //config: HorizontalCalendarConfig = HorizontalCalendarConfig(),
             onSelectedDate: (LocalDate) -> Unit) {


}