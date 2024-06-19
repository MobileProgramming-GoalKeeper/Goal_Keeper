package com.example.goalkeeper.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.goalkeeper.LocalNavGraphViewModelStoreOwner
import com.example.goalkeeper.component.GoalKeeperButton
import com.example.goalkeeper.model.ToDoGroupColor
import com.example.goalkeeper.model.ToDoGroupIcon
import com.example.goalkeeper.model.TodoGroup
import com.example.goalkeeper.style.AppStyles
import com.example.goalkeeper.viewmodel.GoalKeeperViewModel

@Composable
fun GroupScreen(navController: NavHostController) {

    val viewModel: GoalKeeperViewModel =
        viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Group Setting Screen")

        GoalKeeperButton(
            width = 200,
            height = 60,
            text = "그룹 추가",
            textStyle = AppStyles.korTitleStyle
        ) {
            showDialog = true
            //viewModel.insertGroup(TodoGroup())
        }
        Spacer(modifier = Modifier.padding(15.dp))
        GoalKeeperButton(
            width = 200,
            height = 60,
            text = "돌아가기",
            textStyle = AppStyles.korTitleStyle
        ) {
            navController.popBackStack()
        }

    }

    if (showDialog) {
        GroupCreationDialog(
            onDismiss = { showDialog = false },
            onConfirm = { groupName, groupColor, groupIcon ->
                viewModel.insertGroup(TodoGroup(groupName = groupName, color = groupColor.toString(), icon = groupIcon.toString()))
                showDialog = false
            }
        )
    }

}

@Composable
fun GroupCreationDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, ToDoGroupColor, ToDoGroupIcon) -> Unit
) {
    var groupName by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(ToDoGroupColor.RED) }
    var selectedIcon by remember { mutableStateOf(ToDoGroupIcon.FAVORITE) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "그룹 생성") },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(text = "그룹 이름")
                TextField(
                    value = groupName,
                    onValueChange = { groupName = it }
                )

                Spacer(modifier = Modifier.padding(10.dp))

                Text(text = "그룹 컬러")
                Column {
                    ToDoGroupColor.values().forEach { color ->
                        Button(
                            onClick = { selectedColor = color },
                            modifier = Modifier.background(
                                if (selectedColor == color) Color.Gray else Color.Transparent
                            )
                        ) {
                            Text(text = color.toString(), color = color.toColor())
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(10.dp))

                Text(text = "그룹 아이콘")
                Column {
                    ToDoGroupIcon.values().forEach { icon ->
                        Button(
                            onClick = { selectedIcon = icon },
                            modifier = Modifier.background(
                                if (selectedIcon == icon) Color.Gray else Color.Transparent
                            )
                        ) {
                            Text(text = icon.toString())
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(groupName, selectedColor, selectedIcon)
                }
            ) {
                Text(text = "확인")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "취소")
            }
        }
    )
}