package com.example.goalkeeper.component.todo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.goalkeeper.LocalNavGraphViewModelStoreOwner
import com.example.goalkeeper.R
import com.example.goalkeeper.component.ChangeGroupDiaglog
import com.example.goalkeeper.component.EditableText
import com.example.goalkeeper.component.todo.timealarm.setTodoAlarm
import com.example.goalkeeper.component.GoalKeeperTextField
import com.example.goalkeeper.model.MAX_TODO_MEMO
import com.example.goalkeeper.model.MAX_TODO_NAME
import com.example.goalkeeper.model.SubTodo
import com.example.goalkeeper.model.Todo
import com.example.goalkeeper.model.toLocalDateTime
import com.example.goalkeeper.model.toStringFormat
import com.example.goalkeeper.style.AppStyles
import com.example.goalkeeper.style.AppStyles.GroupNameStyle
import com.example.goalkeeper.viewmodel.GoalKeeperViewModel

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
    var notificationTime by remember { mutableStateOf("") } // 알림 시간 저장


    var showChangeGroup by remember { mutableStateOf(false) }
    var showAddSub by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
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
                modifier = Modifier.size(350.dp, 50.dp),
                maxLength = MAX_TODO_MEMO
            )

            TodoMenuRow("시간 알림", R.drawable.ic_notification, {
                showNotificationDialog = true
            })
            TodoMenuRow("내일 하기", R.drawable.ic_arrow_forward) {
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
            TodoMenuRow("날짜/시간 바꾸기", R.drawable.ic_date_range) {
                showDatePicker = true
//                calculateDateTime(todo, selectedDate, viewModel)
            }
            TodoMenuRow("그룹 바꾸기", R.drawable.ic_drive_file_move) {
                showChangeGroup = true
            }
            TodoMenuRow("세부 할 일 추가하기", R.drawable.ic_playlist_add) {
                showAddSub = true
            }
            TodoMenuRow("삭제하기", R.drawable.ic_delete) {
                viewModel.deleteTodoItem(todo)
                navController.popBackStack()
            }

            // 알림 설정 다이얼로그
            if (showNotificationDialog) {
                AlertDialog(
                    onDismissRequest = { showNotificationDialog = false },
                    title = { Text("알림 설정") },
                    text = {
                        Column {
                            if (todo.todoStartAt.isNotEmpty()) {
                                Text("설정된 시간: ${todo.todoStartAt}")
                                Text("이 시간에 알림을 설정할까요?")
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Button(onClick = {
                                        showNotificationDialog = false
                                        todo.todoAlert = true
                                        setTodoAlarm(context, todo) // 알림 설정 함수 호출
                                    }) {
                                        Text("On")
                                    }
                                    Button(onClick = {
                                        showNotificationDialog = false
                                        todo.todoAlert = false
                                        setTodoAlarm(context, todo) // 알림 해제 함수 호출
                                    }) {
                                        Text("Off")
                                    }
                                }
                            } else {
                                Text("시간이 설정되지 않았습니다. 시간을 먼저 설정해주세요.")
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

            if(showAddSub){
                AddSubTodoDialog(
                    onAddSubTodo = {title, memo ->
                        viewModel.insertSubItem(
                            SubTodo(subName = title, subMemo = memo),
                            todo.todoId
                        )
                    },
                    onDismiss = {showAddSub = false},
                    showDialog = showAddSub
                )
            }
        }
    }
}

@Composable
fun AddSubTodoDialog(
    onAddSubTodo: (String, String) -> Unit,
    onDismiss: () -> Unit,
    showDialog: Boolean
) {
    var title by remember { mutableStateOf("") }
    var memo by remember { mutableStateOf("") }

    if (showDialog) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Surface(color = Color.White) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "서브 할 일 추가", style = GroupNameStyle)
                    Spacer(modifier = Modifier.padding(bottom = 8.dp))
                    GoalKeeperTextField(
                        width = 300,
                        height = 60,
                        value = title,
                        label = "서브 할 일",
                        maxLength = MAX_TODO_NAME
                    ) { title = it }
                    Spacer(modifier = Modifier.height(16.dp))
                    GoalKeeperTextField(
                        width = 300,
                        height = 60,
                        value = memo,
                        label = "서브 메모",
                        maxLength = MAX_TODO_MEMO
                    ) { memo = it }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (title.isNotEmpty()) {
                                onAddSubTodo(title, memo)
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
