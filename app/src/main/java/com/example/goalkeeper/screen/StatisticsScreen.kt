package com.example.goalkeeper.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.goalkeeper.LocalNavGraphViewModelStoreOwner
import com.example.goalkeeper.R
import com.example.goalkeeper.component.GoalKeeperButton
import com.example.goalkeeper.component.PieChart
import com.example.goalkeeper.component.PieChartData
import com.example.goalkeeper.model.Todo
import com.example.goalkeeper.model.toStringFormat
import com.example.goalkeeper.style.AppStyles.korTitleStyle
import com.example.goalkeeper.viewmodel.GoalKeeperViewModel
import kotlinx.coroutines.delay
import java.time.LocalDateTime

@Composable
fun StatisticsScreen(navController: NavHostController) {

    val viewModel: GoalKeeperViewModel =
        viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    val todoList by viewModel.todoList.collectAsState()

    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
//        userInfo 아래에 할 일 추가
        val n = 7
        for (i in 1..n)
            viewModel.insertTodo(
                Todo(
                    todoId = i,
                    groupId = 1,
                    todoName = "" + i,
                    todoDate = LocalDateTime.of(2024, 6, 2, 0, 0).toStringFormat(),
                    todoStartAt = LocalDateTime.of(2024, 6, 2, 9, 0).toStringFormat(),
                    todoEndAt = LocalDateTime.of(2024, 6, 2, 10, 30).toStringFormat(),
                    todoAlert = false,
                    todoMemo = "사랑니 빼야됨 ㅜㅜ",
                    todoDone = false,
                    postponedNum = i
                )
            )

        viewModel.loadTodoList(viewModel.user.value!!)
        delay(1000)
        isLoading = false
    }

    if (isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "로고",
                modifier = Modifier.size(180.dp)
            )

            Text(text = "데이터 불러오는 중...", fontSize = 24.sp)
        }
    } else {
        val dataList = todoToPieChartData(todoList)

        val totalPostponed by remember {
            mutableStateOf(dataList.sumOf { it.value })
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(0.5f)
            ) {
                PieChart(
                    modifier = Modifier
                        .size(500.dp),
                    data = dataList,
                    centerText = "할 일 미룬 횟수: $totalPostponed 회"
                )
            }

            PieChartList(itemList = dataList)

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

@Composable
fun PieChartList(itemList: List<PieChartData>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(0.6f)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(itemList) { item ->
            PieChartDataItem(item)
        }
    }
}

@Composable
fun PieChartDataItem(data: PieChartData) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(25.dp, 25.dp)
                .background(color = data.color, shape = RoundedCornerShape(10.dp))
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = data.label,
            fontSize = 20.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = "${data.value}회",
            fontSize = 16.sp
        )
    }
}

fun todoToPieChartData(todoList: List<Todo>): List<PieChartData> {
    val colorList = listOf(
        0xFFBA8A94,
        0xFFC99FA9,
        0xFFCEA6B0,
        0xFFDDBBC5,
        0xFFE7C9D3,
        0xFFECD0DA,
        0xFFF6DEE8,
    )

    val pieChartDataList = mutableListOf<PieChartData>()

    todoList.forEachIndexed { index, todo ->
        pieChartDataList.add(
            PieChartData(
                color = Color(colorList[index % colorList.size]),
                value = todo.postponedNum,
                label = todo.todoName
            )
        )
    }
    return pieChartDataList
}
