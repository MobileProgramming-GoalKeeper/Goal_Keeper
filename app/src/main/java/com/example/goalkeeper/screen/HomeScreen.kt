package com.example.goalkeeper.screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.goalkeeper.LocalNavGraphViewModelStoreOwner
import com.example.goalkeeper.component.todo.ToDoGroupPrint
import com.example.goalkeeper.component.todo.TodoDetailView
import com.example.goalkeeper.viewmodel.GoalKeeperViewModel
import java.time.LocalDate

@Composable
fun HomeScreen() {

    val navController = rememberNavController()
    val viewModel: GoalKeeperViewModel =
        viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    //    테스트 데이터
    LaunchedEffect(Unit) {
        viewModel.setupTestData()
        Log.d("setup","set up Test Data.")
    }

    val groupListState by viewModel.groupList.collectAsState()
    val itemListState by viewModel.todoList.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            //Calender()

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(groupListState) { todoGroup ->
                    ToDoGroupPrint(toDoGroup = todoGroup, viewModel) { todo ->
                        // TodoDetailView로 이동
                        navController.navigate("todoMenu/${todo.todoId}")
                    }
                }
            }

        }
        NavHost(
            navController = navController,
            startDestination = "todoMenu/{todoId}",
            modifier = Modifier.fillMaxSize()
        ) {
            composable(
                route = "todoMenu/{todoId}",
                arguments = listOf(navArgument("todoId") { type = NavType.StringType })
            ) { backStackEntry ->
                val todoId = backStackEntry.arguments?.getString("todoId")
                if (todoId != null) {
                    itemListState.forEach { todo ->

                        val todo = itemListState.find { it.todoId == todoId }
                        if (todo != null) {
                            TodoDetailView(
                                todo = todo,
                                navController = navController,
                                viewModel = viewModel
                            ) { newTodo ->
                                viewModel.updateTodo(todoId, newTodo)
                            }
                        }
                        return@composable
                    }
                }
            }
        }
    }
}


@Composable
fun Calender(
    modifier: Modifier = Modifier,
    currentDate: LocalDate = LocalDate.now(),
    //config: HorizontalCalendarConfig = HorizontalCalendarConfig(),
    onSelectedDate: (LocalDate) -> Unit,
) {


}