package com.example.goalkeeper.component.todo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.goalkeeper.LocalNavGraphViewModelStoreOwner
import com.example.goalkeeper.R
import com.example.goalkeeper.model.MAX_TODO_MEMO_prev
import com.example.goalkeeper.model.SubTodo
import com.example.goalkeeper.model.Todo
import com.example.goalkeeper.style.AppStyles.TodoMemoStyle
import com.example.goalkeeper.style.AppStyles.TodoNameStyle
import com.example.goalkeeper.viewmodel.GoalKeeperViewModel

@Composable
fun TodoRow(todo: Todo, navController: NavController) {
    val viewModel: GoalKeeperViewModel =
        viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    val subTodoList by viewModel.subTodoList.collectAsState()
    var subTodos by remember(subTodoList) {
        mutableStateOf(subTodoList.filter { it.rootTodoId == todo.todoId })
    }
    LaunchedEffect(subTodoList) {
        subTodos = subTodoList.filter { it.rootTodoId == todo.todoId }
    }

    var name by remember { mutableStateOf(todo.todoName) }
    var memo by remember { mutableStateOf(todo.todoMemo.take(MAX_TODO_MEMO_prev)) } //처음 30글자만
    var isDone by remember { mutableStateOf(todo.todoDone) }

    val prevTodo by remember { mutableStateOf(todo) }
    if (prevTodo != todo) {
        name = todo.todoName
        memo = todo.todoMemo.take(MAX_TODO_MEMO_prev)
        isDone = todo.todoDone
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isDone,
                onCheckedChange = { checked ->
                    isDone = checked
                    viewModel.updateDoneTodoItem(todo.copy(todoDone = checked))
                }
            )
            Column {
                Text(text = name, style = TodoNameStyle)
                Text(text = memo, style = TodoMemoStyle)
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painterResource(id = R.drawable.ic_more_horiz),
                contentDescription = "todoMenu",
                modifier = Modifier
                    .padding(end = 10.dp)
                    .clickable { navController.navigate("todoMenu/${todo.todoId}") }
            )
        }
        // SubTodos 출력
        subTodos.forEach { subTodo ->
            SubTodoRow(subTodo = subTodo, navController = navController)
        }
    }
}

@Composable
fun SubTodoRow(subTodo: SubTodo, navController: NavController) {
    val viewModel: GoalKeeperViewModel =
        viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    var name by remember { mutableStateOf(subTodo.subName) }
    var memo by remember { mutableStateOf(subTodo.subMemo.take(MAX_TODO_MEMO_prev)) } // 처음 30글자만
    var isDone by remember { mutableStateOf(subTodo.subDone) }

    val prevSubTodo by remember { mutableStateOf(subTodo) }
    if (prevSubTodo != subTodo) {
        name = subTodo.subName
        memo = subTodo.subMemo.take(MAX_TODO_MEMO_prev)
        isDone = subTodo.subDone
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp), // 공백 추가
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isDone,
            onCheckedChange = { checked ->
                isDone = checked
                viewModel.updateSubItem(subTodo.copy(subDone=checked))
            }
        )
        Column {
            Text(text = name, style = TodoNameStyle)
            Text(text = memo, style = TextStyle(
                fontFamily = FontFamily(Font(R.font.freesentation_regular)),
                fontSize = 10.sp,
                color = Color.DarkGray)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(id = R.drawable.ic_more_horiz),
            contentDescription = "subTodoMenu",
            modifier = Modifier
                .padding(end = 10.dp)
                .clickable { navController.navigate("subTodoMenuView/${subTodo.subTodoId}") }
        )
    }
}