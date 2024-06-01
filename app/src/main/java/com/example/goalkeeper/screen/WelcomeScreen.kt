package com.example.goalkeeper.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.goalkeeper.LocalNavGraphViewModelStoreOwner
import com.example.goalkeeper.R
import com.example.goalkeeper.component.GoalKeeperButton
import com.example.goalkeeper.component.GoalKeeperTextField
import com.example.goalkeeper.nav.Routes
import com.example.goalkeeper.style.AppStyles.loginTextStyle
import com.example.goalkeeper.viewmodel.GoalKeeperViewModel

@Composable
fun WelcomeScreen(navController: NavHostController) {

    val viewModel: GoalKeeperViewModel =
        viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)

    var userID by remember { mutableStateOf("") }
    var userPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "로고",
            modifier = Modifier.size(180.dp)
        )

        GoalKeeperTextField(width = 300, height = 60, value = userID, label = "ID") { userID = it }
        Spacer(modifier = Modifier.padding(5.dp))
        GoalKeeperTextField(
            width = 300,
            height = 60,
            value = userPassword,
            label = "PASSWORD"
        ) { userPassword = it }

        Spacer(modifier = Modifier.padding(20.dp))
        GoalKeeperButton(
            width = 230,
            height = 60,
            text = "로그인/시작하기",
            textStyle = loginTextStyle
        ) {
            if (viewModel.login(userID, userPassword))
                navController.navigate(Routes.Main.route)
        }
        Spacer(modifier = Modifier.padding(10.dp))
        GoalKeeperButton(
            width = 230,
            height = 60,
            text = "회원가입",
            textStyle = loginTextStyle
        ) {
            navController.navigate(Routes.Register.route)
        }
    }
}

