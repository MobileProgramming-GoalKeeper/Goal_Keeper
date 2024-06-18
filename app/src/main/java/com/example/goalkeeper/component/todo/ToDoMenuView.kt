package com.example.goalkeeper.component.todo

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.SemanticsProperties.EditableText
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.goalkeeper.LocalNavGraphViewModelStoreOwner
import com.example.goalkeeper.R
import com.example.goalkeeper.component.ChangeGroupDiaglog
import com.example.goalkeeper.component.EditableText
import com.example.goalkeeper.model.MAX_TODO_MEMO
import com.example.goalkeeper.model.MAX_TODO_NAME
import com.example.goalkeeper.model.Todo
import com.example.goalkeeper.model.toLocalDateTime
import com.example.goalkeeper.model.toStringFormat
import com.example.goalkeeper.style.AppStyles
import com.example.goalkeeper.viewmodel.GoalKeeperViewModel
import com.example.goalkeeper.viewmodel.TodoRepository

@Composable
fun TodoDetailView(
    todo: Todo,
    navController: NavController,
) {
    val context = LocalContext.current

    val viewModel: GoalKeeperViewModel =
        viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("") }
    var showNotificationDialog by remember { mutableStateOf(false) }

    var showChangeGroup by remember { mutableStateOf(false) }

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
            Icon(imageVector = Icons.Filled.Star,
                contentDescription = "star",
                tint = if(todo.bookmark) colorResource(id = R.color.Star_yellow) else Color.LightGray,
                modifier = Modifier
                    .size(60.dp)
                    .padding(10.dp)
                    .clickable {
                        viewModel.updateBookmarkTodoItem(todo.copy(bookmark = !todo.bookmark))
                    }
            )
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
                    viewModel.updateNameTodoItem(updatedTodo)
                },
                style = AppStyles.TodoMenuTitleStyle,
                maxLength = MAX_TODO_NAME
            )

            if (todo.todoStartAt != "") {
                Text(
                    text = todo.todoStartAt,
                    style = AppStyles.TodoMenuDateStyle
                )
            } else {
                Text(
                    text = todo.todoDate,
                    style = AppStyles.TodoMenuDateStyle
                )
            }

            Spacer(modifier = Modifier.padding(5.dp))
            EditableText(
                text = todo.todoMemo,
                onTextChange = {
                    val updatedTodo = todo.copy(todoMemo = it)
                    viewModel.updateMemoTodoItem(updatedTodo)
                },
                style = AppStyles.TodoMemoStyle,
                modifier = Modifier.size(350.dp, 100.dp),
                maxLength = MAX_TODO_MEMO
            )

            TodoMenuRow("시간 알림", Icons.Filled.Notifications, {
                showNotificationDialog = true
            })
            TodoMenuRow("내일 하기", Icons.AutoMirrored.Filled.ArrowForward) {
                viewModel.updatePostponeTodoItem(todo)
                val newDate = todo.todoDate.toLocalDateTime().plusDays(1).toStringFormat(false)
                viewModel.updateDateTodoItem(todo, newDate)
                if (todo.todoStartAt.isNotEmpty()) {
                    val newStartAt =
                        todo.todoStartAt.toLocalDateTime().plusDays(1).toStringFormat(true)
                    val newEndAt = todo.todoEndAt.toLocalDateTime().plusDays(1).toStringFormat(true)
                    viewModel.updateTimeTodoItem(todo, newStartAt, newEndAt)
                }
            }
            TodoMenuRow("날짜/시간 바꾸기", Icons.Filled.DateRange) {
                showDatePicker = true
//                calculateDateTime(todo, selectedDate, viewModel)
            }
            TodoMenuRow("그룹 바꾸기", Icons.Filled.ExitToApp) {
                showChangeGroup = true
            }
            TodoMenuRow("삭제하기", Icons.Filled.Delete) {
                viewModel.deleteTodoItem(todo)
                navController.popBackStack()
            }

            if (showNotificationDialog) {
                AlertDialog(
                    onDismissRequest = { showNotificationDialog = false },
                    title = { Text("알림 설정") },
                    text = {
                        Column {
                            Text("알림을 설정하시겠습니까?")
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Button(onClick = {
                                    showNotificationDialog = false
                                    //showDatePicker = true
                                }) {
                                    Text("예")
                                }
                                Button(onClick = { showNotificationDialog = false }) {
                                    Text("아니오")
                                }
                            }
                        }
                    },
                    confirmButton = {},
                    dismissButton = {}
                )
            }

            if (showDatePicker) {
                ChangeDate(todo = todo,
                    showDatePicker = showDatePicker,
                    onDismissRequest = {
                        showDatePicker = false
                    },
                    onDateSelected = { newDate ->
                        selectedDate = newDate
                        if (selectedDate.contains(":")) { //시간 정보가 있을때
                            if(todo.todoStartAt!="") { //기존 startAt이 있으면 -> 시간간격 계산 가능
                                val durationBetweenStartAndEnd =
                                    todo.todoEndAt.toLocalDateTime().let {
                                        java.time.Duration.between(
                                            todo.todoStartAt.toLocalDateTime(),
                                            it
                                        )
                                    }
                                val newEndAt = durationBetweenStartAndEnd.let {
                                    selectedDate.toLocalDateTime().plus(it)
                                }
                                viewModel.updateTimeTodoItem(
                                    todo,
                                    selectedDate,
                                    newEndAt.toStringFormat(true)
                                )
                            }else{ //기존 startAt이 없다->시간 간격 계산 불가
                                viewModel.updateTimeTodoItem(todo, selectedDate, selectedDate)
                            }
                        }else{ //시간정보 없다 -> 사용자가 시각 설정 안함
                            viewModel.updateTimeTodoItem(todo, "", "")
                        }
                        viewModel.updateDateTodoItem(todo, selectedDate)
                    },
                    onEndDateSelected = {selectedEndDate->
                        viewModel.updateTimeTodoItem(todo,selectedDate, selectedEndDate)
                    }
                )
            }
            if(showChangeGroup){
                ChangeGroupDiaglog(
                    onChangeGroup = {selectedGroupId ->
                        viewModel.updateGroupIdTodoItem(
                            todo, todo.copy(groupId = selectedGroupId))
                    },
                    onDismiss = { showChangeGroup = false },
                    todo = todo,
                    showDialog = showChangeGroup
                )
            }
        }
    }
}
