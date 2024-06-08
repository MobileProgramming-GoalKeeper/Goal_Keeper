package com.example.goalkeeper.component.todo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.SemanticsProperties.EditableText
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.goalkeeper.LocalNavGraphViewModelStoreOwner
import com.example.goalkeeper.component.EditableText
import com.example.goalkeeper.model.MAX_TODO_MEMO
import com.example.goalkeeper.model.MAX_TODO_NAME
import com.example.goalkeeper.model.Todo
import com.example.goalkeeper.style.AppStyles
import com.example.goalkeeper.viewmodel.GoalKeeperViewModel

@Composable
fun TodoDetailView(
    todo: Todo,
    navController: NavController,
    viewModel: GoalKeeperViewModel,
    onTodoChange: (Todo) -> Unit
) {
    val context = LocalContext.current
    val viewModel: GoalKeeperViewModel =
        viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "뒤로가기",
                modifier = Modifier
                    .size(60.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = "공유(복사)",
                modifier = Modifier
                    .size(60.dp)
                    .padding(10.dp)
                    .clickable {
                        copyToClipboard(context, todo)
                    }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 25.dp, end = 25.dp)
        ) {
            Spacer(modifier = Modifier.padding(5.dp))
            EditableText(
                text = todo.todoName,
                onTextChange = {
                    val updatedTodo = todo.copy(todoName = it)
                    onTodoChange(updatedTodo)
                },
                style = AppStyles.TodoMenuTitleStyle,
                maxLength = MAX_TODO_NAME
            )
            todo.todoStartAt?.let{
                Text(
                    text = todo.todoStartAt!!,
                    style = AppStyles.TodoMenuDateStyle
                )
            } ?: run {
                Text(
                    text = todo.todoDate,
                    style = AppStyles.TodoMenuDateStyle)
            }

            Spacer(modifier = Modifier.padding(5.dp))
            EditableText(
                text = todo.todoMemo,
                onTextChange = {
                    val updatedTodo = todo.copy(todoMemo =  it)
                    onTodoChange(updatedTodo)
                },
                style = AppStyles.TodoMemoStyle,
                modifier = Modifier.size(350.dp, 100.dp),
                maxLength = MAX_TODO_MEMO
            )

            TodoMenuRow("시간 알림", Icons.Filled.Notifications, {})
            TodoMenuRow("내일 하기", Icons.AutoMirrored.Filled.ArrowForward, {
                viewModel.updatePostponeTodoItem(todo)
                val newTodo = DoItTomorrow(todo)
                viewModel.updateTodo(todo.todoId, newTodo)
            })
            TodoMenuRow("날짜 바꾸기", Icons.Filled.DateRange, {})
            TodoMenuRow("그룹 바꾸기", Icons.Filled.ExitToApp, {})
            TodoMenuRow("삭제하기", Icons.Filled.Delete, {
                viewModel.deleteTodoItem(todo)
                navController.popBackStack()
            })
        }
    }
}
