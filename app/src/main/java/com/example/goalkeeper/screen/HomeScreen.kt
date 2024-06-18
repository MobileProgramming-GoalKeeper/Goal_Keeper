package com.example.goalkeeper.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.YearMonth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import java.time.format.TextStyle
import java.util.Locale
import androidx.compose.ui.graphics.Color


@Composable
fun HomeScreen() {

    val navController = rememberNavController()
    val viewModel: GoalKeeperViewModel =
        viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)


    //    테스트 데이터
//    LaunchedEffect(Unit) {
//        viewModel.setupTestData()
//        Log.d("setup","set up Test Data.")
//    }

    val groupListState by viewModel.groupList.collectAsState()
    val todoListState by viewModel.todoList.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Calender(
                modifier = Modifier.padding(16.dp),
                onSelectedDate = { selectedDate ->
                    // 선택한 날짜 처리
                    Log.d("Calender", "Selected date: $selectedDate")
                }
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(groupListState) { todoGroup ->
                    ToDoGroupPrint(toDoGroup = todoGroup) { todo ->
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
                    todoListState.forEach { todo ->
                        val todo = todoListState.find { it.todoId == todoId }
                        if (todo != null) {
                            TodoDetailView(
                                todo = todo,
                                navController = navController
                            )
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
    onSelectedDate: (LocalDate) -> Unit,
) {
    var selectedDate by remember { mutableStateOf(currentDate) }
    var currentMonth by remember { mutableStateOf(YearMonth.of(currentDate.year, currentDate.month)) }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Previous month",
                modifier = Modifier.clickable {
                    currentMonth = currentMonth.minusMonths(1)
                }
            )
            Text(
                text = "${currentMonth.year}년 ${currentMonth.monthValue}월",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = "Next month",
                modifier = Modifier.clickable {
                    currentMonth = currentMonth.plusMonths(1)
                }
            )
        }

        DaysOfWeek()

        val firstDayOfMonth = currentMonth.atDay(1)
        val lastDayOfMonth = currentMonth.atEndOfMonth()
        val daysOfMonth = (1..lastDayOfMonth.dayOfMonth).map { currentMonth.atDay(it) }
        val startOffset = firstDayOfMonth.dayOfWeek.value % 7

        Column {
            // 첫 번째 주
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                repeat(startOffset) {
                    Spacer(modifier = Modifier.size(40.dp))
                }
                daysOfMonth.take(7 - startOffset).forEach { day ->
                    Day(
                        day = day,
                        isSelected = day == selectedDate,
                        onClick = {
                            selectedDate = day
                            onSelectedDate(day)
                        }
                    )
                }
            }

            // 나머지 주
            daysOfMonth.drop(7 - startOffset).chunked(7).forEach { week ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    week.forEach { day ->
                        Day(
                            day = day,
                            isSelected = day == selectedDate,
                            onClick = {
                                selectedDate = day
                                onSelectedDate(day)
                            }
                        )
                    }
                    // 빈 칸 채우기
                    repeat(7 - week.size) {
                        Spacer(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun DaysOfWeek() {
    val daysOfWeek = listOf("일", "월", "화", "수", "목", "금", "토")
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
        daysOfWeek.forEach { day ->
            Text(
                text = day,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.size(40.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun Day(
    day: LocalDate,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color.Black else Color.Transparent
    val textColor = if (isSelected) Color.White else Color.Black

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(40.dp)
            .background(backgroundColor, shape = CircleShape)
            .clickable(onClick = onClick)
    ) {
        Text(text = day.dayOfMonth.toString(), color = textColor)
    }
}