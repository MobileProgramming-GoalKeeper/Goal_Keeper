package com.example.goalkeeper.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
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
import com.example.goalkeeper.LocalNavGraphViewModelStoreOwner
import com.example.goalkeeper.R
import com.example.goalkeeper.model.MAX_TODO_NAME
import com.example.goalkeeper.model.NotificationType
import com.example.goalkeeper.model.UserRoutine
import com.example.goalkeeper.notification.scheduleRoutineNotification
import com.example.goalkeeper.style.AppStyles.korTitleStyle
import com.example.goalkeeper.viewmodel.GoalKeeperViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineDialog(onDismiss: () -> Unit) {
    val viewModel: GoalKeeperViewModel =
        viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    var routineName by remember { mutableStateOf("") }

    var notificationType: NotificationType by remember { mutableStateOf(NotificationType.DAILY) }

    var userRoutine: UserRoutine? by remember { mutableStateOf(null) }

    val timePickerState = rememberTimePickerState(initialHour = 8, initialMinute = 0)

    if (userRoutine != null) {
        Log.d("RoutineDialog", userRoutine.toString())
        scheduleRoutineNotification(LocalContext.current, userRoutine!!)
        onDismiss()
    }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(40.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Spacer(modifier = Modifier.padding(bottom = 4.dp))
                GoalKeeperTextField(
                    width = 300,
                    height = 60,
                    value = routineName,
                    label = "루틴 이름",
                    maxLength = MAX_TODO_NAME
                ) { routineName = it }

                Spacer(modifier = Modifier.height(20.dp))
                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = Color.LightGray,
                        clockDialSelectedContentColor = Color.White,
                        selectorColor = colorResource(id = R.color.violet),
                        periodSelectorBorderColor = Color.Black,
                        periodSelectorSelectedContainerColor = colorResource(id = R.color.violet),
                        periodSelectorSelectedContentColor = Color.White,
                        timeSelectorSelectedContainerColor = colorResource(id = R.color.violet),
                        timeSelectorUnselectedContainerColor = colorResource(id = R.color.light_pink),
                        timeSelectorSelectedContentColor = Color.White
                    )
                )

                Row(
                    modifier = Modifier
                        .padding(start = 14.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = notificationType == NotificationType.DAILY,
                        onCheckedChange = {
                            notificationType = NotificationType.DAILY
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = colorResource(id = R.color.violet),
                            uncheckedColor = Color.LightGray,
                            checkmarkColor = Color.White
                        )
                    )
                    Text(text = "매일")

                    Checkbox(
                        checked = notificationType == NotificationType.WEEKDAY,
                        onCheckedChange = {
                            notificationType = NotificationType.WEEKDAY
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = colorResource(id = R.color.violet),
                            uncheckedColor = Color.LightGray,
                            checkmarkColor = Color.White
                        )
                    )
                    Text(text = "평일")

                    Checkbox(
                        checked = notificationType == NotificationType.WEEKEND,
                        onCheckedChange = {
                            notificationType = NotificationType.WEEKEND
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = colorResource(id = R.color.violet),
                            uncheckedColor = Color.LightGray,
                            checkmarkColor = Color.White
                        )
                    )
                    Text(text = "주말")
                }

                Spacer(modifier = Modifier.padding(4.dp))

                GoalKeeperButton(width = 120, height = 40, text = "추가", textStyle = korTitleStyle) {
                    if (routineName.isNotEmpty()) {
                        userRoutine = UserRoutine(
                            routineName = routineName,
                            routineAlert = true,
                            hour = timePickerState.hour,
                            minute = timePickerState.minute,
                            notificationType = notificationType
                        )

                        viewModel.insertRoutine(userRoutine!!)
                    }
                }
            }
        }
    }
}





