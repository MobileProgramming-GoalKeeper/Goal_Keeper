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

@Composable
fun ToDoGroupPrint(toDoGroup: TodoGroup, onMenuIconClick:(Todo) -> Unit) {
    var todos by remember { mutableStateOf(toDoGroup.mainTodo) }

    Box(contentAlignment = Alignment.CenterStart) {
        TopRoundedRectangle(500, 50, colorResource(id = R.color.bright_gray))
        Row {
            Spacer(modifier = Modifier.padding(start = 20.dp))
            Icon(
                imageVector = toDoGroup.icon.imgVector,
                contentDescription = toDoGroup.groupName,
                tint = toDoGroup.color.toColor()
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
    /* //구분선
        Divider(
            color = Color.LightGray, // 색상
            thickness = 2.dp, // 두께
            modifier = Modifier
                .padding(horizontal = 16.dp)// 양쪽 패딩
                .padding(vertical = 16.dp)
        )
     */

}

