package com.example.goalkeeper.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.goalkeeper.LocalNavGraphViewModelStoreOwner
import com.example.goalkeeper.model.Todo
import com.example.goalkeeper.style.AppStyles
import com.example.goalkeeper.viewmodel.GoalKeeperViewModel

@Composable
fun ChangeGroupDiaglog(
    onChangeGroup: (String) -> Unit,
    onDismiss: () -> Unit,
    todo: Todo,
    showDialog: Boolean,
) {
    val viewModel: GoalKeeperViewModel =
        viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)
    val groupListState by viewModel.groupList.collectAsState()
    val group = groupListState.find { it.groupId == todo.groupId }

    var groupName by remember { mutableStateOf(group!!.groupName) }
    var selectedGroupId by remember { mutableStateOf(todo.groupId) }

    if (showDialog) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Surface(color = Color.White) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "그룹 바꾸기", style = AppStyles.GroupNameStyle)
                    Spacer(modifier = Modifier.padding(bottom = 8.dp))
                    Text(text = "선택한 그룹 : ${groupName}", style = AppStyles.TodoNameStyle)
                    Spacer(modifier = Modifier.padding(bottom = 8.dp))
                    LazyVerticalGrid(columns = GridCells.Fixed(5)) {
                        items(groupListState) { todoGroup ->
                            TodoGroupBtn(toDoGroup = todoGroup) {
                                groupName = todoGroup.groupName
                                selectedGroupId = todoGroup.groupId
                            }
                        }
                    }
                    Button(
                        onClick = {
                            onChangeGroup(selectedGroupId)
                            onDismiss()
                        }
                    ) {
                        Text(text = "확인")
                    }
                }
            }
        }

    }
}