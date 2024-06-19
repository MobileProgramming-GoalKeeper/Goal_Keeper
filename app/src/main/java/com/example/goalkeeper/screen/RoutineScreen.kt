package com.example.goalkeeper.screen

import android.Manifest
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.goalkeeper.LocalNavGraphViewModelStoreOwner
import com.example.goalkeeper.R
import com.example.goalkeeper.component.GoalKeeperButton
import com.example.goalkeeper.component.RoutineDialog
import com.example.goalkeeper.model.NotificationType
import com.example.goalkeeper.model.UserRoutine
import com.example.goalkeeper.notification.cancelRoutineNotification
import com.example.goalkeeper.notification.scheduleRoutineNotification
import com.example.goalkeeper.style.AppStyles.korTitleStyle
import com.example.goalkeeper.viewmodel.GoalKeeperViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RoutineScreen(navController: NavHostController) {
    val context = LocalContext.current

    val viewModel: GoalKeeperViewModel =
        viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    val routineList by viewModel.routineList.collectAsState()

    val notificationPermission =
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

    LaunchedEffect(true) {
        if (!notificationPermission.status.isGranted)
            notificationPermission.launchPermissionRequest()
    }

    LaunchedEffect(key1 = routineList) {
        viewModel.loadRoutineList()
    }

    var showAddRoutineDialog by remember {
        mutableStateOf(false)
    }

    if (showAddRoutineDialog) {
        RoutineDialog { showAddRoutineDialog = false }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = colorResource(id = R.color.violet),
                onClick = { showAddRoutineDialog = true }) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Routine",
                    tint = colorResource(id = R.color.white)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(routineList) { routine ->
                RoutineItem(routine = routine,
                    toggleAlarm = {
                        viewModel.updateRoutineAlert(routine)
                        scheduleRoutineNotification(context, routine)
                    },
                    onDelete = {
                        viewModel.deleteRoutine(routine)
                        scheduleRoutineNotification(context, routine)
                    }
                )
            }
            item {
                Spacer(modifier = Modifier.padding(10.dp))
                GoalKeeperButton(
                    width = 200,
                    height = 60,
                    text = "돌아가기",
                    textStyle = korTitleStyle
                ) {
                    navController.popBackStack()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineItem(routine: UserRoutine, toggleAlarm: () -> Unit = {}, onDelete: () -> Unit = {}) {
    var routineAlert by remember {
        mutableStateOf(routine.routineAlert)
    }

    var showRoutineDialog by remember {
        mutableStateOf(false)
    }

    if (showRoutineDialog) {
        Dialog(onDismissRequest = { showRoutineDialog = false }) {
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
                    val context = LocalContext.current

                    val viewModel: GoalKeeperViewModel =
                        viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

                    val timePickerState = rememberTimePickerState(initialHour = routine.hour, initialMinute = routine.minute)

                    var notificationType: NotificationType by remember { mutableStateOf(routine.notificationType) }

                    Text(text = "루틴 수정", fontSize = 20.sp, modifier = Modifier.padding(10.dp))

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
                        routine.hour = timePickerState.hour
                        routine.minute = timePickerState.minute
                        routine.notificationType = notificationType
                        viewModel.insertRoutine(routine)

                        if(routine.routineAlert) cancelRoutineNotification(context, routine)

                        scheduleRoutineNotification(context, routine)
                        showRoutineDialog = false
                    }
                }
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                showRoutineDialog = true
            },
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.light_pink)
        ),
        shape = RoundedCornerShape(30.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = routine.routineName, fontSize = 20.sp)
                Text(
                    text = "${routine.hour}시 ${routine.minute}분, ${
                        when (routine.notificationType) {
                            NotificationType.DAILY -> "매일"
                            NotificationType.WEEKDAY -> "평일"
                            NotificationType.WEEKEND -> "주말"
                        }
                    }마다",
                    fontSize = 14.sp
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "ToggleAlarm",
                    modifier = Modifier.clickable {
                        routine.routineAlert = !routine.routineAlert
                        routineAlert = routine.routineAlert
                        toggleAlarm()
                    },
                    tint = if (routineAlert) Color.Red else Color.Gray
                )
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "deleteRoutine",
                    modifier = Modifier.clickable {
                        onDelete()
                    },
                    tint = colorResource(id = R.color.violet)
                )
            }
        }
    }
}
