package com.example.goalkeeper.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.goalkeeper.LocalNavGraphViewModelStoreOwner
import com.example.goalkeeper.R
import com.example.goalkeeper.component.GoalKeeperCheckBox
import com.example.goalkeeper.model.Todo
import com.example.goalkeeper.style.AppStyles
import com.example.goalkeeper.ui.theme.Blue1
import com.example.goalkeeper.ui.theme.Pink1
import com.example.goalkeeper.ui.theme.Yellow1
import com.example.goalkeeper.viewmodel.GoalKeeperViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@Composable
fun TimeScreen() {
    var isSwitchOn by rememberSaveable { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "시간표 모드",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 30.dp),
                )
                Switch(
                    checked = isSwitchOn,
                    onCheckedChange = { isSwitchOn = it },
                    Modifier.padding(end = 30.dp)
                )
            }
            Spacer(modifier = Modifier.height(90.dp))
            if (isSwitchOn) {
                TimeOnScreen()
            } else {
                TimeOffScreen()
            }
        }
    }
}

@Composable
fun TimeOnScreen() {
    val viewModel: GoalKeeperViewModel =
        viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
    val todayString = today.format(formatter) // 2024년 06월 12일
    val hours = (0..24).map { if (it == 0) "" else "%02d:00".format(it - 1) }
    val minutes = listOf("", "0~", "10~", "20~", "30~", "40~", "50~")
    val todoListState by viewModel.todoList.collectAsState()
    var showPopup by remember { mutableStateOf(false) }
    var selectedTodos by remember { mutableStateOf<List<Todo>>(emptyList()) }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            for (i in hours.indices) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    for (j in minutes.indices) {
                        val cellTodos = if (i != 0 && j != 0) {
                            val hour = i - 1
                            val minute = (j - 1) * 10
                            todoListState.filter { todo ->
                                val todoDateOnly = todo.todoDate.takeWhile { it != '일' }
                                    .plus("일")
                                val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm")
                                val todoStart = LocalDateTime.parse(todo.todoStartAt, formatter)
                                val todoEnd = LocalDateTime.parse(todo.todoEndAt, formatter)
                                val cellTime = LocalDateTime.of(today, LocalTime.of(hour, minute))
                                todoDateOnly == todayString && cellTime >= todoStart && cellTime < todoEnd
                            }
                        } else emptyList()
                        val backgroundColor = when {
                            cellTodos.any { it.bookmark } -> Yellow1
                            cellTodos.size > 1 -> Blue1
                            cellTodos.size == 1 -> Pink1
                            else -> Color.Transparent
                        }
                        Box(
                            modifier = Modifier
                                .width(50.dp)
                                .height(40.dp)
                                .border(1.dp, Color.Black)
                                .background(backgroundColor)
                                .clickable {
                                    if (cellTodos.isNotEmpty()) {
                                        selectedTodos = cellTodos.sortedBy { todo ->
                                            LocalDateTime.parse(
                                                todo.todoStartAt,
                                                DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm")
                                            )
                                        }
                                        showPopup = true
                                    }
                                }
                                .padding(all = 0.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (i == 0 && j == 0) {
                                // 빈 칸 (왼쪽 상단)
                                Text(text = "")
                            } else if (i == 0) {
                                // 첫 번째 행 (간격 표시)
                                Text(text = minutes[j])
                            } else if (j == 0) {
                                // 첫 번째 열 (시간 표시)
                                Text(text = hours[i])
                            } else {
                                // 나머지 칸 (시간표 셀)
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    if (showPopup) {
        ShowPopup(selectedTodos) {
            showPopup = false
        }
    }
}

@Composable
fun ShowPopup(todoList: List<Todo>, onDismiss: () -> Unit) {
    val popupTitle = "할 일 목록"
    val popupContent = buildAnnotatedString {
        todoList.forEach { todo ->
            withStyle(style = ParagraphStyle()) {
                append(todo.todoName + "\n")
            }
        }
    }
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = popupTitle,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp  // 이 부분은 필요에 따라 추가했습니다.
                )
            )
        },
        text = {
            Text(
                text = popupContent,
                style = TextStyle(textAlign = TextAlign.Start),
                fontSize = 20.sp
            )
        },
        confirmButton = {
            Button(
                onClick = { onDismiss() },
            ) {
                Text(text = "확인")
            }
        }
    )
}

@Composable
fun TimeOffScreen() {
    val viewModel: GoalKeeperViewModel =
        viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)
    val todoListState by viewModel.todoList.collectAsState()
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
    val todayString = today.format(formatter)
    val todayTodos = todoListState.filter {
        val todoDateOnly = it.todoDate.takeWhile { it != '일' }
            .plus("일")
        todoDateOnly == todayString
    }.sortedBy { todo -> todo.todoStartAt }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(todayTodos) { todo ->
                    ShowTodo(todo = todo)
                }
            }
        }
    }
}

@Composable
fun ShowTodo(todo: Todo) {
    val viewModel: GoalKeeperViewModel =
        viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)
    var name by remember { mutableStateOf(todo.todoName) }
    var isDone by remember { mutableStateOf(todo.todoDone) }
    var startAt by remember { mutableStateOf(todo.todoStartAt) }
    val prevTodo by remember { mutableStateOf(todo) }
    if (prevTodo != todo) {
        name = todo.todoName
        startAt = todo.todoStartAt
        isDone = todo.todoDone
    }
    Box(
        modifier = Modifier
            .padding(bottom = 20.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .padding(start = 20.dp, end = 20.dp)
                .background(
                    color = colorResource(id = R.color.light_pink),
                    shape = RoundedCornerShape(60.dp),
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.padding(10.dp))
            GoalKeeperCheckBox(
                checked = isDone,
                onCheckedChange = { checked ->
                    isDone = checked
                    viewModel.updateDoneTodoItem(todo.copy(todoDone = checked))
                }
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Column {
                Text(text = name, style = AppStyles.TodoNameStyle, fontSize = 30.sp)
                Text(text = startAt, style = AppStyles.TodoMemoStyle, fontSize = 20.sp)
            }
        }
    }
}