package com.example.goalkeeper.component.todo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.goalkeeper.LocalNavGraphViewModelStoreOwner
import com.example.goalkeeper.R
import com.example.goalkeeper.component.GoalKeeperTextField
import com.example.goalkeeper.component.TopRoundedRectangle
import com.example.goalkeeper.model.*
import com.example.goalkeeper.style.AppStyles
import com.example.goalkeeper.model.Todo
import com.example.goalkeeper.style.AppStyles.GroupNameStyle
import com.example.goalkeeper.viewmodel.GoalKeeperViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@Composable
fun ToDoGroupPrint(
    toDoGroup: TodoGroup,
    todos: List<Todo> = emptyList(),
    navController: NavController
) {
    val viewModel: GoalKeeperViewModel =
        viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    val colorEnum = ToDoGroupColor.valueOf(toDoGroup.color)
    val iconEnum = ToDoGroupIcon.valueOf(toDoGroup.icon)

    var showAddDialog by remember { mutableStateOf(false) }

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
            Text(text = toDoGroup.groupName, style = GroupNameStyle)
            Spacer(modifier = Modifier.padding(5.dp))
            Icon(imageVector = Icons.Filled.Add,
                contentDescription = "todo add button",
                modifier = Modifier
                    .background(color = Color.White, shape = CircleShape)
                    .clickable {
                        showAddDialog = true
                    }
            )
            if (showAddDialog){
                AddTodoDialog(
                    onAddTodo = { title, memo, date, endDate ->
                        var startAt = ""
                        var endAt=""
                        if(date.contains(":")){
                            startAt = date
                            if(endDate==""){
                                endAt = date.toLocalDateTime().plusMinutes(10).toString()
                            }else{
                                endAt = endDate
                            }
                        }
                        val newTodo = Todo(
                            todoId = "new",
                            groupId = toDoGroup.groupId,
                            todoName = title,
                            todoDate = date.toLocalDateTime().toStringFormat(false),
                            todoStartAt = startAt,
                            todoEndAt = endAt,
                            todoMemo = memo)
                        viewModel.insertTodoItem(newTodo)
                    },
                    onDismiss = { showAddDialog = false },
                    toDoGroup = toDoGroup,
                    showDialog = showAddDialog
                )
            }

        }

    }
    Column {
        todos.forEach { todo ->
            TodoRow(todo = todo, navController = navController)
        }
    }
    Spacer(modifier = Modifier.padding(vertical = 16.dp))
}

@Composable
fun AddTodoDialog(
    onAddTodo: (String, String, String, String) -> Unit,
    onDismiss: () -> Unit,
    toDoGroup: TodoGroup,
    showDialog: Boolean
) {
    var title by remember { mutableStateOf("") }
    var memo by remember { mutableStateOf("") }

    var showDatePickerDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("날짜 선택하기") }
    var selectedEndDate by remember { mutableStateOf("") }

    if (showDialog) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Surface(color = Color.White) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "할 일 추가", style = GroupNameStyle)
                    Spacer(modifier = Modifier.padding(bottom = 8.dp))
                    GoalKeeperTextField(
                        width = 300,
                        height = 60,
                        value = title,
                        label = "할 일",
                        maxLength = MAX_TODO_NAME
                    ) { title = it }
                    Spacer(modifier = Modifier.height(16.dp))
                    GoalKeeperTextField(
                        width = 300,
                        height = 60,
                        value = memo,
                        label = "메모",
                        maxLength = MAX_TODO_MEMO
                    ) { memo = it }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { showDatePickerDialog = true  }) {
                        Text(text = "날짜 선택하기")
                    }

                    if(showDatePickerDialog){
                        ChangeDate(
                            todo = Todo(
                                todoId = "new",
                                groupId = toDoGroup.groupId,
                                todoName = title,
                                todoDate = LocalDateTime.now().toStringFormat(time=true),
                                todoMemo = memo
                            ),
                            showDatePicker = showDatePickerDialog,
                            onDismissRequest = { showDatePickerDialog = false },
                            onDateSelected = { selectedDate = it },
                            onEndDateSelected = { selectedEndDate=it }
                        )
                    }

                    Button(
                        onClick = {
                            if (title.isNotEmpty() && selectedDate != "날짜 선택하기") {
                                onAddTodo(title, memo, selectedDate, selectedEndDate)
                            }
                            onDismiss()
                        }
                    ) {
                        Text("확인")
                    }
                }
            }
        }
    }
}