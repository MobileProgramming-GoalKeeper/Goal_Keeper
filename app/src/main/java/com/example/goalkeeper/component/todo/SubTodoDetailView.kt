package com.example.goalkeeper.component.todo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.goalkeeper.LocalNavGraphViewModelStoreOwner
import com.example.goalkeeper.component.EditableText
import com.example.goalkeeper.model.MAX_TODO_MEMO
import com.example.goalkeeper.model.MAX_TODO_MEMO_prev
import com.example.goalkeeper.model.MAX_TODO_NAME
import com.example.goalkeeper.model.SubTodo
import com.example.goalkeeper.style.AppStyles
import com.example.goalkeeper.viewmodel.GoalKeeperViewModel
@Composable
fun SubTodoDetailView(
    navController: NavController,
    subTodo: SubTodo
) {
    val viewModel: GoalKeeperViewModel = viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)
    var name by remember { mutableStateOf(subTodo.subName) }
    var memo by remember { mutableStateOf(subTodo.subMemo.take(MAX_TODO_MEMO_prev)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
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
        Spacer(modifier = Modifier.height(16.dp))

        EditableText(
            text = name,
            onTextChange = {
                name = it
                viewModel.updateSubItem(subTodo.copy(subName = it))
            },
            style = AppStyles.TodoMenuTitleStyle,
            maxLength = MAX_TODO_NAME
        )

        Spacer(modifier = Modifier.height(16.dp))

        EditableText(
            text = memo,
            onTextChange = {
                memo = it
                viewModel.updateSubItem(subTodo.copy(subMemo = it))
            },
            style = AppStyles.TodoMemoStyle,
            modifier = Modifier.size(350.dp, 100.dp),
            maxLength = MAX_TODO_MEMO
        )

        Spacer(modifier = Modifier.height(16.dp))

        TodoMenuRow("삭제하기", Icons.Filled.Delete) {
            viewModel.deleteSubItem(subTodo)
            navController.popBackStack()
        }
    }
}