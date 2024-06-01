package com.example.goalkeeper.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.goalkeeper.component.GoalKeeperButton
import com.example.goalkeeper.style.AppStyles

@Composable
fun ThemeColorScreen(navController: NavHostController) {

//    val viewModel: GoalKeeperViewModel =
//        viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "ThemeColor Screen")

        GoalKeeperButton(
            width = 200,
            height = 60,
            text = "돌아가기",
            textStyle = AppStyles.korTitleStyle
        ) {
            navController.popBackStack()
        }
    }
}

