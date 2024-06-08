package com.example.goalkeeper.component.todo

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.goalkeeper.R
import com.example.goalkeeper.model.Todo
import com.example.goalkeeper.model.toLocalDateTime
import com.example.goalkeeper.model.toStringFormat
import com.example.goalkeeper.style.AppStyles
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun DoItTomorrow(todo: Todo): Todo {
    val newDate = todo.todoDate.toLocalDateTime().plusDays(1)

    val newStartAt = todo.todoStartAt?.toLocalDateTime()?.plusDays(1)?.toStringFormat(true)
    val newEndAt = todo.todoEndAt?.toLocalDateTime()?.plusDays(1)?.toStringFormat(true)

    return todo.copy(
        todoDate = newDate.toStringFormat(if (newStartAt==null)false else true),
        todoStartAt = newStartAt,
        todoEndAt = newEndAt
    )
}

fun copyToClipboard(context: Context, todo: Todo) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("ToDo", todo.toText())
    clipboard.setPrimaryClip(clip)

    Toast.makeText(context, "클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show()
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ChangeDate(
    todo: Todo,
    showDatePicker: Boolean,
    onDismissRequest: () -> Unit,
    onDateSelected: (String) -> Unit
){

    val datePickerState = rememberDatePickerState()

    var selectedHour by remember { mutableStateOf(LocalDateTime.now().hour) }
    var selectedMinute by remember { mutableStateOf((LocalDateTime.now().minute / 10) * 10) }
    var selectedDateTime by remember { mutableStateOf(todo.todoDate.toLocalDateTime()) }

    var showTimePicker by remember { mutableStateOf(false) }

//    var timeInfo: Boolean = false

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { onDismissRequest() },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedDate = datePickerState.selectedDateMillis?.let { millis ->
                            LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(millis),
                                ZoneId.systemDefault()
                            ).toLocalDate()
                        } ?: LocalDate.now()

                        val selectedTime = LocalTime.of(selectedHour, selectedMinute)
                        selectedDateTime = LocalDateTime.of(selectedDate, selectedTime)

                        showTimePicker = true
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        TimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedDate = selectedDateTime.toLocalDate()
                        val selectedTime = LocalTime.of(selectedHour, selectedMinute)
                        selectedDateTime = LocalDateTime.of(selectedDate, selectedTime)

                        showTimePicker = false
                        onDateSelected(selectedDateTime.toStringFormat(true))
                        onDismissRequest()
//                        timeInfo = true
//                        // 사용자가 선택한 날짜 및 시간 정보 처리
//                        println("Selected Date and Time: $selectedDateTime")
                    }
                ) { Text("OK", style = AppStyles.TodoNameStyle) }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showTimePicker = false
//                        timeInfo = false
                        onDismissRequest()
                    }
                ) { Text("시간 설정 안함", style = AppStyles.TodoNameStyle) }
            },
            selectedHour = selectedHour,
            selectedMinute = selectedMinute,
            onHourSelected = { hour -> selectedHour = hour },
            onMinuteSelected = { minute -> selectedMinute = minute }
        )
    }
//    if (!timeInfo) selectedDateTime.toLocalDate()
//    return selectedDateTime.toStringFormat(timeInfo)

}

@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable (() -> Unit),
    dismissButton: @Composable (() -> Unit)? = null,
    selectedHour: Int,
    selectedMinute: Int,
    onHourSelected: (Int) -> Unit,
    onMinuteSelected: (Int) -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            shape = RoundedCornerShape(size = 20.dp),
            tonalElevation = 6.dp,
            modifier = Modifier.wrapContentSize()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Selected Time: ${selectedHour}시 ${selectedMinute}분",
                    style = AppStyles.GroupNameStyle
                )
                Spacer(modifier = Modifier.padding(top = 16.dp, bottom = 5.dp))
                CustomTimeInput(
                    selectedHour = selectedHour,
                    onHourSelected = onHourSelected,
                    selectedMinute = selectedMinute,
                    onMinuteSelected = onMinuteSelected
                )
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    dismissButton?.invoke()
                    confirmButton()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTimeInput(
    selectedHour: Int,
    onHourSelected: (Int) -> Unit,
    selectedMinute: Int,
    onMinuteSelected: (Int) -> Unit,
) {
    val hours = (0..23).toList()
    val minutes = (0..5).map { it * 10 }
    val hourListState = rememberLazyListState()
    val minuteListState = rememberLazyListState()

    Row(horizontalArrangement = Arrangement.Center) {
        LazyColumn(
            state = hourListState,
            modifier = Modifier
                .weight(1f)
                .height(150.dp), // 높이를 제한하여 3-5개의 항목만 보이도록 설정
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(hours) { hour ->
                Button(
                    onClick = { onHourSelected(hour) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedHour == hour) colorResource(id = R.color.violet) else Color.White,
                        contentColor = if (selectedHour == hour) Color.White else Color.Black
                    )
                ) {
                    Text(
                        text = "$hour 시",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.freesentation_regular)),
                            fontSize = 20.sp
                        )
                    )
                }
            }
        }
        LazyColumn(
            state = minuteListState,
            modifier = Modifier
                .weight(1f)
                .height(150.dp), // 높이를 제한하여 3-5개의 항목만 보이도록 설정
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(minutes) { minute ->
                Button(
                    onClick = { onMinuteSelected(minute) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedMinute == minute) colorResource(id = R.color.violet) else Color.White,
                        contentColor = if (selectedMinute == minute) Color.White else Color.Black
                    )
                ) {
                    Text(
                        text = "$minute 분",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.freesentation_regular)),
                            fontSize = 20.sp
                        )
                    )
                }
            }
        }
    }
}