package com.example.goalkeeper.component.todo

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
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
import com.example.goalkeeper.style.AppStyles.GroupNameStyle
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Locale


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
    onDateSelected: (String) -> Unit,
    onEndDateSelected: (String) -> Unit, // EndTime을 선택했을때
) {

    val datePickerState = rememberDatePickerState()

    var selectedHour by remember { mutableStateOf(if (todo.todoStartAt != "") todo.todoStartAt.toLocalDateTime().hour else LocalDateTime.now().hour) }
    var selectedMinute by remember { mutableStateOf(if (todo.todoStartAt != "") todo.todoStartAt.toLocalDateTime().minute else LocalDateTime.now().minute / 10 * 10) }
    var selectedDateTime by remember { mutableStateOf(if (todo.todoStartAt != "") todo.todoStartAt.toLocalDateTime() else todo.todoDate.toLocalDateTime()) }
    var showTimePicker by remember { mutableStateOf(false) }

    var selectedEndHour by remember { mutableStateOf(if (todo.todoEndAt != "") todo.todoEndAt.toLocalDateTime().hour else LocalDateTime.now().plusMinutes(10).hour) }
    var selectedEndMinute by remember { mutableStateOf(if (todo.todoEndAt != "") todo.todoEndAt.toLocalDateTime().minute else LocalDateTime.now().plusMinutes(10).minute / 10 * 10) }
    var selectedEndDateTime by remember { mutableStateOf(if (todo.todoEndAt != "") todo.todoEndAt.toLocalDateTime() else todo.todoDate.toLocalDateTime().plusMinutes(10)) }
    var showEndTimePicker by remember { mutableStateOf(false) }

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
//
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
            dialogTitle = "시작 시각 선택하기",
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedDate = selectedDateTime.toLocalDate()
                        val selectedTime = LocalTime.of(selectedHour, selectedMinute)
                        selectedDateTime = LocalDateTime.of(selectedDate, selectedTime)

                        showTimePicker = false
                        onDateSelected(selectedDateTime.toStringFormat(true))
                        showEndTimePicker = true
                    }
                ) { Text("OK", style = AppStyles.TodoNameStyle) }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDateSelected(selectedDateTime.toStringFormat(false))
                        showTimePicker = false
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
    if (showEndTimePicker) {
        TimePickerDialog(
            dialogTitle = "종료 시각 선택하기",
            onDismissRequest = { showEndTimePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedDate = selectedDateTime.toLocalDate()
                        val selectedTime = LocalTime.of(selectedEndHour, selectedEndMinute)
                        selectedEndDateTime = LocalDateTime.of(selectedDate, selectedTime)

                        showEndTimePicker = false
                        onEndDateSelected(selectedEndDateTime.toStringFormat(true))
                        onDismissRequest()
                    }
                ) { Text("OK", style = AppStyles.TodoNameStyle) }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onEndDateSelected(selectedDateTime.plusMinutes(10).toStringFormat(true))
                        showEndTimePicker = false
                        onDismissRequest()
                    }
                ) { Text("종료 시각 설정 안함", style = AppStyles.TodoNameStyle) }
            },
            selectedHour = selectedEndHour,
            selectedMinute = selectedEndMinute,
            onHourSelected = { hour -> selectedEndHour = hour },
            onMinuteSelected = { minute -> selectedEndMinute = minute }
        )
    }
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
    dialogTitle: String
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
                Text(text = dialogTitle, style=GroupNameStyle)
                Text(
                    text = "Selected Time: ${selectedHour}시 ${selectedMinute}분",
                    style = AppStyles.TodoMemoStyle
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

//fun setAlarmForNotification(context: Context, dateTime: String) {
//    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//    val intent = Intent(context, AlarmReceiver::class.java)
//    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
//    val timeInMillis = sdf.parse(dateTime)?.time ?: 0L
//
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
//    } else {
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
//    }
//
//    Toast.makeText(context, "알림이 설정되었습니다.", Toast.LENGTH_SHORT).show()
//}
//
//fun cancelAlarm(context: Context) {
//    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//    val intent = Intent(context, AlarmReceiver::class.java)
//    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//    alarmManager.cancel(pendingIntent)
//    pendingIntent.cancel()
//
//    Toast.makeText(context, "알림이 취소되었습니다.", Toast.LENGTH_SHORT).show()
//}