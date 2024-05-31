package com.example.goalkeeper.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.goalkeeper.R
import com.example.goalkeeper.Style.loginTextStyle
import com.example.goalkeeper.component.RectWithRoundedEnds
import com.example.goalkeeper.component.WelcomeTextField

@Composable
fun WelcomeScreen(navController: NavHostController) {

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

        WelcomeTextField(width = 300, height = 60, value = userID, label = "ID") { userID = it }
        Spacer(modifier = Modifier.padding(5.dp))
        WelcomeTextField(width = 300, height = 60, value = userPassword, label = "PASSWORD") { userPassword = it }
        Spacer(modifier = Modifier.padding(20.dp))

        Box(
            modifier = Modifier.clickable { navController.navigate("main") },
            contentAlignment = Alignment.Center
        ) {
            RectWithRoundedEnds(230, 60, color = colorResource(id = R.color.violet))
            androidx.compose.material3.Text(text = "로그인/시작하기", style = loginTextStyle)
        }
    }
}

