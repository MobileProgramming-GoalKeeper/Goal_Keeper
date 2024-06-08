package com.example.goalkeeper.component.todo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.goalkeeper.R
import com.example.goalkeeper.component.TopRoundedRectangle
import com.example.goalkeeper.model.*
import com.example.goalkeeper.style.AppStyles
import com.example.goalkeeper.model.Todo
import com.example.goalkeeper.viewmodel.GoalKeeperViewModel

@Composable
fun ToDoGroupPrint(
    toDoGroup: TodoGroup,
    viewModel: GoalKeeperViewModel,
    onMenuIconClick:(Todo) -> Unit)
{
    val colorEnum = ToDoGroupColor.valueOf(toDoGroup.color)
    val iconEnum = ToDoGroupIcon.valueOf(toDoGroup.icon)

    val itemList by viewModel.todoList.collectAsState(initial = emptyList())
    val groupList by viewModel.groupList.collectAsState(initial = emptyList())

    var todos = itemList.filter { it.groupId == toDoGroup.groupId }
    val selectedGroup = groupList.find { it.groupId == toDoGroup.groupId }

    LaunchedEffect(todos, selectedGroup) {
        todos = itemList.filter { it.groupId == selectedGroup?.groupId }
    }
    Box(contentAlignment = Alignment.CenterStart) {
        TopRoundedRectangle(500, 50, colorResource(id = R.color.bright_gray))
        Row {
            Spacer(modifier = Modifier.padding(start = 20.dp))
            Icon(
                imageVector = iconEnum.imgVector,
                contentDescription = toDoGroup.groupName,
                tint = colorEnum.toColor()
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Text(text = toDoGroup.groupName, style = AppStyles.GroupNameStyle)
            Spacer(modifier = Modifier.padding(5.dp))


            Icon(imageVector = Icons.Filled.Add,
                contentDescription = "group add button",
                modifier = Modifier
                    .background(color = Color.White, shape = CircleShape)
                    .clickable {
                        //이 그룹에 toDo add
                    })

        }

    }
    Column {
        todos.forEach { todo ->
            TodoRow(todo = todo) {
                onMenuIconClick(todo)
            }
        }
    }
    Spacer(modifier = Modifier.padding(vertical = 16.dp))
}

