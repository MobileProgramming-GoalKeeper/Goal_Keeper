package com.example.goalkeeper.component.todo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.goalkeeper.model.Todo
import com.example.goalkeeper.style.AppStyles.TodoMemoStyle
import com.example.goalkeeper.style.AppStyles.TodoNameStyle

@Composable
fun TodoRow(todo: Todo, onMenuIconClicked: (Todo) -> Unit) {
    var name by remember { mutableStateOf(todo.todoName) }
    var memo by remember { mutableStateOf(todo.todoMemo) }
    var isDone by remember { mutableStateOf(todo.todoDone) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isDone,
            onCheckedChange = { checked ->
                isDone = checked
                todo.todoDone = checked
            }
        )
        Column {
            Text(text = name, style = TodoNameStyle)
            Text(text = memo, style = TodoMemoStyle)
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = "todoMenu",
            modifier = Modifier
                .padding(end = 10.dp)
                .clickable { onMenuIconClicked(todo) }
        )
    }

}

@Composable
fun ChangeToDoTime(todo: Todo) {
//    var startAt by remember { mutableStateOf(todo.startAt) }

}