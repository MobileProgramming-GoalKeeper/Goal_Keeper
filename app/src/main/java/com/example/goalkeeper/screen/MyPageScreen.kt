package com.example.goalkeeper.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.goalkeeper.LocalNavGraphViewModelStoreOwner
import com.example.goalkeeper.R
import com.example.goalkeeper.component.GoalKeeperButton
import com.example.goalkeeper.nav.Routes
import com.example.goalkeeper.style.AppStyles.korTitleStyle
import com.example.goalkeeper.viewmodel.GoalKeeperViewModel

@Composable
fun MyPageScreen() {
    val navController = rememberNavController()

    val viewModel: GoalKeeperViewModel =
        viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.padding(15.dp))
        NavHost(navController = navController, startDestination = Routes.MyPage.route) {
            composable(Routes.MyPage.route) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = viewModel.userID,
                        fontSize = 40.sp,
                        modifier = Modifier.padding(20.dp),
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.freesentation_8extrabold)),
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            color = Color.Black
                        )
                    )

                    GoalKeeperButton(
                        width = 300,
                        height = 60,
                        text = "통계",
                        textStyle = korTitleStyle
                    ) {
                        navController.navigate(Routes.Statistics.route)
                    }
                    Spacer(modifier = Modifier.padding(15.dp))
                    GoalKeeperButton(
                        width = 300,
                        height = 60,
                        text = "루틴",
                        textStyle = korTitleStyle
                    ) {
                        navController.navigate(Routes.Routine.route)
                    }
                    Spacer(modifier = Modifier.padding(15.dp))
                    GoalKeeperButton(
                        width = 300,
                        height = 60,
                        text = "테마색",
                        textStyle = korTitleStyle
                    ) {
                        navController.navigate(Routes.ThemeColor.route)
                    }
                }
            }
            composable(Routes.Statistics.route) { StatisticsScreen(navController) }
            composable(Routes.Routine.route) { RoutineScreen(navController) }
            composable(Routes.ThemeColor.route) { ThemeColorScreen(navController) }
        }
    }
}